/*
    This code should be included along with
    dashboard.js to have instructor controls
    over students' codes
 */

/*
    Line colors are stored in following arrays
    once hover-in is made on code lines
 */
let oldLineColors = [];

/*
    First and last intervals for new suggestions
 */
let firstMarker = -1, lastMarker = -1;

// Marked line color
const markedColor = "#DBDBDB";

$( document ).ready(function() {
    $("#instructorButton").css("display", "block");
    $("#showGrade").css("display", "none");
});

/*
    Following line converts comment editor
    button from warning to error and vice versa
 */
function toggleSuggestionType () {
    if ($(event.target).text() === "Warning") {
        $(event.target).html("Error");
        $(event.target).removeClass("btn-warning");
        $(event.target).addClass("btn-danger");
    } else {
        $(event.target).html("Warning");
        $(event.target).removeClass("btn-danger");
        $(event.target).addClass("btn-warning");
    }
}

/*
    Turns on or off new suggestion editor view
 */
function adjustView (ind) {
    if (ind === "newContent") {
        $("#notification-div").hide();
        $("#comment-panel").hide();
        $("#comment-editor-wrapper").show();
        $("#reply-editor-wrapper").hide();
        $(".reply-panel").hide();
        $("#suggestion-remove-btn").hide();
    } else {
        let editorWrapper = $("#reply-editor-wrapper");
        if (!editorWrapper.is(":visible")) {
            $("#notification-div").hide();
            $("#comment-panel").show();
            $("#comment-editor-wrapper").hide();
            editorWrapper.show();
            $(".reply-panel").show();
            $("#suggestion-remove-btn").show();
        }
    }
}

/*
    Line number hover-in function
 */
function inHover (event) {
    //Get index of line number
    let index = $(event.currentTarget).parent().parent().index()+1;
    let col = $(event.currentTarget).css("background-color");
    if (col === warningColor || col === errorColor || col === markedColor) {
        col = "";
    }
    oldLineColors[index] = col;
    $(event.currentTarget).css("background-color", markedColor);
}


/*
    Line number hover-out function
 */
function outHover (event) {
    let index = $(event.currentTarget).parent().parent().index()+1;
    if (index === lastMarker) {
        return;
    }
    $(event.currentTarget).css("background-color", oldLineColors[index]);
}

/*
    Helps creating new interval
 */
function lineOnClick (eventHandler) {
    let lineNumber = eventHandler.data;
    console.log(lineNumber);

    /*
        If new interval is already chosen
        this function should do nothing
     */
    activeSuggestionID = -1;

    if (lastMarker !== -1) {
        return;
    }

    /*
        Place first marker or
        create new interval if
        first marker is already put
     */
    if (firstMarker === -1) {
        firstMarker = lineNumber;
    } else {
        lastMarker = lineNumber;
        markSuggestion();
    }
}

/*
    Marks marks new suggestion
 */
function markSuggestion () {
    if (!isValidSuggestion()) {
        alert("Suggestion intersects with another suggestion or first index is greater than last index. Please choose correct interval!");
        firstMarker = -1;
        lastMarker = -1;
        return;
    }

    markLines (markedColor);
    adjustView("newContent");
}

/*
    Marks all lines in new interval
 */
function markLines (col) {
    for (let i = firstMarker; i < lastMarker + 1; i ++) {
        console .log(i);
        $(lines[i]).css("background-color", col);
    }
}

/*
    New interval validator
 */
function isValidSuggestion () {
    if (firstMarker > lastMarker) {
        return false;
    }
    for (let i = 0; i < suggestions.length; i ++) {
        if (!(suggestions[i].startInd < firstMarker || suggestions[i].endInd > lastMarker)) {
            return false;
        }
    }
    return true;
}

/*
    Once Clear Suggestion button is clicked
    in suggestion editor panel this function will
    clear code lines and toggle interval editor view
 */
function clearInterval () {
    markLines("");
    firstMarker = -1;
    lastMarker = -1;
    $("#notification-div").show();
    $("#comment-editor-wrapper").hide();
}

function submitSuggestion () {
    toggleLoading();
    let suggestionContent = suggestionEditor.parseContent();
    let suggestionType = $("#suggestion-type");

    if (suggestionContent.length === 0) {
        alert("You can't submit empty suggestion!");
        toggleLoading();
        return;
    }

    let dataObj = {
        type: suggestionType.text(),
        courseID: getParameter("courseID"),
        codeFileID: activeCodeFileID,
        content: suggestionContent,
        startInd: firstMarker,
        endInd: lastMarker
    };

    console.log(suggestionContent);
    console.log(suggestionType.html());
    $.ajax({
        url: "/user/suggestion_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(dataObj),
        success: function(data){
            suggestionEditor.setContent("");
            if (data.type === "Error") {
                data.color = errorColor;
            } else {
                data.color = warningColor;
            }
            suggestions[suggestions.length] = data;
            console.log(suggestions);
            viewSuggestion(suggestions.length-1);
            placeSuggestion(suggestions.length-1, data);
            toggleLoading();
        },
        error: function (data, textStatus, jQxhr) {
            suggestionAdditionError(data, textStatus, jQxhr);
            toggleLoading();
        }});
}

function getInd (id) {
    for (let i = 0; i < suggestions.length; i ++) {
        if (suggestions[i].suggestionID === id) {
            return i;
        }
    }
    return -1;
}

function removeSuggestion (id) {
    let ind = getInd (id);
    let start = suggestions[ind].startInd;
    let end = suggestions[ind].endInd;

    suggestions[ind].startInd = -1;
    suggestions[ind].endInd = -1;

    /*
       Edit suggestion lines
    */
    for (let lineIterator = start; lineIterator < end + 1; lineIterator++) {
        $(lines[lineIterator]).css("background-color", "#f7f7f7");
        $(lines[lineIterator]).css("color", "#999999");
        $(lines[lineIterator]).unbind("click");

        $(lines[lineIterator]).click(lineIterator, lineOnClick);
    }
}

function suggestionAdditionError () {
    alert ("Couldn't add new suggestion");
}

function deleteClick () {
    toggleLoading();
    suggID = activeSuggestionID;
    $.ajax({
        url: "/user/suggestion_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({courseID: getParameter("courseID"), codeFileID: activeCodeFileID, suggestionID: activeSuggestionID}),
        success: function(){
            removeSuggestion(suggID);
            $("#comment-panel").hide();
            $(".reply-panel-wrapper").hide();
            activeSuggestionID = -1;
            toggleLoading();
        },
        error: function () {
            suggestRemoveError();
            toggleLoading();
        }});
}

function suggestRemoveError () {
    alert ("Couldn't remove suggestion");
}

function updateGrade(selectObj) {

    if (window.confirm("Do You want to grade assignments?")) {
        let selectIndex=selectObj.selectedIndex;
        let grade=selectObj.options[selectIndex].text;
        $.ajax({
            url: "/user/update_grade",
            method: "POST",
            data: {'grade': grade,
                'assignmentID' : getParameter("assignmentID"),
                'userID' : getParameter("userID")
            }
        });
    } else {
        $('#instructorSelector').prop('selectedIndex' , 0);
    }
}