let codeMirror;

let suggestionEditor;
let replyEditor;

let codeIDs;
//Array for storing suggestion objects
let suggestions = [];
//HTML line element array
var lines = [];

let codeInfo = [];

let activeSuggestionID = -1;
let activeCodeFileID = -1;

//New reply block
var replyBlock = "                        <div class = \"reply-panel-wrapper\">\n" +
    "                            <div class = \"reply-panel\">\n" +
    "                                <img class = \"reply-profile-picture\" src=\"../Images/temp_user_icon.svg\">\n" +
    "                                <div class = \"reply-content\">\n" +
    "                                    <p class = \"reply-user-name\">Fname Lname</p>\n" +
    "                                    <p class = \"reply-text\">placeHolder</p>\n" +
    "                                    <p class = \"reply-date\">Here goes Precise Date</p>\n" +
    "                                </div>\n" +
    "                            </div>\n" +
    "                        </div>";

//Error and warning line colorings
const errorColor = "#aa6664";
const warningColor = "#efcf4f";


//AJAX successful code loading response callback
function loadCode(data) {
    var receivedData = data;
    suggestions = receivedData.suggestions;

    console.log(receivedData);
    activeCodeFileID = receivedData.fileId;
    codeMirror.setValue(receivedData.code);

    mapCodeLines();
    placeSuggestions();
}

/*
    Places suggestions in appropriate line intervals
 */
function placeSuggestions() {
    for (let i = 0; i < suggestions.length; i++) {

        if (suggestions[i].type === "Error") {
            suggestions[i].color = errorColor;
        } else {
            suggestions[i].color = warningColor;
        }

        placeSuggestion (i, suggestions[i]);
    }
}

function placeSuggestion (ind, suggestion) {
    const start = suggestion.startInd;
    const end = suggestion.endInd;

    /*
        Edit suggestion lines
     */
    for (let lineIterator = start; lineIterator < end + 1; lineIterator++) {
        $(lines[lineIterator]).css("background-color", suggestion.color);
        $(lines[lineIterator]).css("color", "#ffffff");
        $(lines[lineIterator]).unbind("click");
        $(lines[lineIterator]).click(ind, viewSuggestion);
    }
}

/*
    Once any suggestion line is clicked
    following function will show suggestion.
    Adjusts view for suggestion and fetches
    reply data for this suggestion
 */
function viewSuggestion(eventHandler) {
    /*
        Adjust view.
        If student is logged in following lines
        throw function not defined exceptions
     */
    try {
        clearInterval();
        $("#comment-editor-wrapper").hide();
        adjustView("oldContent");
    } catch (e) {

    }
    let dv = $("#notification-div");
    if (dv.is(":visible")) {
        dv.hide();
        $("#comment-panel").show();
        $("#reply-editor-wrapper").show();
    }

    //Load suggestion data
    var suggestion = suggestions[eventHandler.data];
    $("#comment-content").css("border-right-color", suggestion.color);
    $("#comment-text").html(suggestion.content);
    $("#comment-user-name").html(suggestion.user.firstName + " " + suggestion.user.lastName);
    $("#comment-profile-picture").attr("src", suggestion.user.picturePath);
    $("#comment-date").html(suggestion.timeStamp);
    activeSuggestionID = suggestion.suggestionID;
    console.log(activeSuggestionID);

    fetchReplies(suggestion.suggestionID);
}

/*
    Loads new code in CodeMirror editor
 */
function navbarOnClick() {
    console.log("There was a Click!");

    //Change html elements' styles
    $("#navbar").find(".active").removeClass("active");
    $(event.target).parent().attr("class", "active");

    //Get name of code
    var name = $(event.target).text();

    fetchCode(name);
}

//Sends AJAX request to fetch code names
function fetchCodesInfo() {
    toggleLoading();
    let uid = getParameter("userID");
    let assId = getParameter("assignmentID");
    if (uid !== null) {
        dataObj = {
            assignmentID: assId,
            userID: uid
        }
    } else {
        dataObj = {
            assignmentID: assId
        }
    }
    console.log(JSON.stringify(dataObj));
    $.ajax({
        url: "/user/code_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(dataObj),
        success: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadCodesInfo(data, textStatus, jQxhr);
        },
        error: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadCodeInfoError(data, textStatus, jQxhr);
        }
    });
}

//AJAX successful code info loading response callback
function loadCodesInfo(data) {
    codeInfo = data;
    codeInfo.sort(function (a, b) {
        return (a.value < b.value ? -1 : (a.value > b.value ? 1 : 0))
    });
    console.log(codeInfo);
    addCodes();
}

function addCodes() {
    let ul = document.createElement("ul");
    $('#jstree_demo_div')[0].appendChild(ul);
    draw_view_rec(ul, 0, codeInfo.length - 1);
    build_project_view();
}

function loadCodeInfoError() {
    alert("Error in loading file information");
}

//Sends AJAX request to load code
function fetchCode(id) {
    toggleLoading();
    let dataObj;
    dataObj = {
        codeID: id
    };


    $.ajax({
        url: "/user/code_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(dataObj),
        success: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadCode(data, textStatus, jQxhr);
        },
        error: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadCodeError(data, textStatus, jQxhr);
        }
    });
}


//Sends AJAX request to fetch new replies
function fetchReplies(id) {
    toggleLoading();
    console.log(id);
    $.ajax({
        url: "/user/reply_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8/json',
        data: JSON.stringify({courseID: getParameter("courseID"), suggestionID: id}),
        success: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadReplies(data, textStatus, jQxhr);
        },
        error: function (data, textStatus, jQxhr) {
            toggleLoading();
            loadReplyError(data, textStatus, jQxhr);
        }
    });
}

//AJAX successful callback for receiving reply data
function loadReplies(data) {
    console.log(data);
    clearReplies();
    for (let i = 0; i < data.length; i++) {
        drawReply(data[i]);
    }
}

//AJAX error callback for receiving reply data
function loadReplyError(data) {
    console.log(data);
    alert("Couldn't get replies");
}

//Clears all current replies
function clearReplies() {
    $(".reply-panel-wrapper").remove();
}

//Draws one new reply in the suggestion panel
function drawReply(reply) {
    var newBlock = $(replyBlock).closest(".reply-panel-wrapper");
    console.log(reply);

    $(newBlock).find(".reply-user-name").html(reply.user.firstName + " " + reply.user.lastName);
    $(newBlock).find(".reply-profile-picture").attr("src", reply.user.picturePath);
    $(newBlock).find(".reply-text").html(reply.content);
    $(newBlock).find(".reply-date").html(reply.timeStamp);

    $(newBlock).insertBefore("#reply-editor-wrapper");
}

/*
    Maps appropriate code lines for intervals
 */
function mapCodeLines() {
    $(".CodeMirror-linenumber").each(function (index) {
        if (!index) {
            return;
        }

        /*
            Following lines throw exception for student.
         */
        try {
            $(this).click(index, lineOnClick);
            $(this).hover(inHover, outHover);
        } catch (e) {

        }
        lines[index] = this;
    });
}

//AJAX error callback for receiving code data
function loadCodeError() {
    alert("Couldn't load requested file!");
}

/*
    Creates CodeMirror and Bootstrap Markdown editors
 */
function onLoad() {
    //Create CodeMirror editor
    codeMirror = CodeMirror.fromTextArea($("#code-content").get(0), {
        lineNumbers: true,
        mode: "text/x-c++src",
        readOnly: true,
        viewportMargin: Infinity
    });
    codeMirror.setSize("100%", "85%");

    //Create Bootstrap Markdown editor for suggestion editor
    $("#comment-editor-content").markdown({
        autofocus: true,
        saveable: true,
        onShow: function (e) {
            suggestionEditor = e;
        }
    });

    //Create Bootstrap Markdown editor for reply editor
    $("#reply-editor-content").markdown({
        autofocus: true,
        saveable: true,
        onShow: function (e) {
            replyEditor = e;
        }
    });
    /*
    Gets initial data for already
    activated code
    */
    fetchCodesInfo();
    toggleLoading();

}

function submitReply() {
    toggleLoading();
    var replyContent = replyEditor.parseContent();
    console.log(replyContent);
    $("#reply-editor-content").html("");

    $.ajax({
        url: "/user/reply_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            courseID: getParameter("courseID"),
            suggestionID: activeSuggestionID,
            content: replyEditor.parseContent()
        }),
        success: function (data) {
            toggleLoading();
            drawReply(data);
        },
        error: function (data, textStatus, jQxhr) {
            toggleLoading();
            showReplyAdditionError(data, textStatus, jQxhr);
        }
    });
}

function showReplyAdditionError() {
    alert("Couldn't add new reply, please make sure that correct suggestion is chosen!")
}

function getParameter(name) {
    results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    return results[1] || 0;
}

function toggleLoading() {
    $("#loader-wrapper").fadeToggle();
}