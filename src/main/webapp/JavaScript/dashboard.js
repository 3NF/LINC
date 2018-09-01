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

//Error and warning line colorings
const errorColor = "#aa6664";
const warningColor = "#efcf4f";

const codeContentSuffix = "_content";
//const codeRepliesSuffix = "_replies";
const recentSuggestionSuffix = "_suggestion";

const replyContentThreshold = 300;
let maxReplyLength = 16384;

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

//WebSocket for sending and receiving reply data
let replySocket = new WebSocket("ws://" + document.location.host + "/reply_socket");

replySocket.onopen = function () {
    console.log("WebSocket have connected to server endpoint");
};

replySocket.onmessage = function (event) {
    console.log("Reply received");
    var replyData = JSON.parse(event.data);
    if (activeSuggestionID === replyData.suggestionID) {
        console.log(replyData);
        drawReply(replyData);
    }
};

replySocket.onerror = function () {
    alert("Couldn't add new reply, please make sure that correct suggestion is chosen!")
};

function submitReply() {
    var data = {
        courseID: getParameter("courseID"),
        suggestionID: activeSuggestionID,
        content: replyEditor.parseContent()
    };

    if (data.content.length === 0) {
        alert("You can't submit empty reply!");
        toggleLoading();
        return;
    }

    if (data.content.length > maxReplyLength) {
        alert("Reply content is too big!");
        return;
    }

    replySocket.send(JSON.stringify(data));
    console.log("Reply submited");
    replyEditor.setContent("");
}

//AJAX successful code loading response callback
function loadCode(receivedData, reqObj) {
    suggestions = receivedData.suggestions;

    console.log(receivedData);
    activeCodeFileID = receivedData.fileId;
    if (reqObj.needsContent) {
        codeMirror.setValue(receivedData.code);
        sessionStorage.setItem(reqObj.codeID + codeContentSuffix, receivedData.code)
    } else {
        codeMirror.setValue(sessionStorage.getItem(reqObj.codeID + codeContentSuffix));
    }

    codeMirror.setOption("mode", getMode(receivedData.name));

    mapCodeLines();
    placeSuggestions();
}

//TODO: Implementation for other modes
function getMode(name) {
    return "text/x-java";
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

        placeSuggestion(i, suggestions[i]);
    }
}

function placeSuggestion(ind, suggestion) {
    const start = suggestion.startInd;
    const end = suggestion.endInd;

    /*
        Edit suggestion lines
     */
    for (let lineIterator = start; lineIterator < end + 1; lineIterator++) {
        $(lines[lineIterator]).css("background-color", suggestion.color);
        $(lines[lineIterator]).css("color", "#ffffff");
        $(lines[lineIterator]).unbind("click");
        $(lines[lineIterator]).click(function () {
            viewSuggestion(ind);
        });
    }
}

/*
    Once any suggestion line is clicked
    following function will show suggestion.
    Adjusts view for suggestion and fetches
    reply data for this suggestion
 */
function viewSuggestion(ind) {
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
    $("#comment-panel").show();
    $("#reply-editor-wrapper").show();


    //Load suggestion data
    var suggestion = suggestions[ind];
    console.log(suggestion);
    $("#comment-content").css("border-right-color", suggestion.color);
    $("#comment-text").html(suggestion.content);
    $("#comment-user-name").html(suggestion.user.firstName + " " + suggestion.user.lastName);
    $("#comment-profile-picture").attr("src", suggestion.user.picturePath);
    $("#comment-date").html(suggestion.timeStamp);
    activeSuggestionID = suggestion.suggestionID;
    localStorage.setItem(suggestion.fileID + recentSuggestionSuffix, JSON.stringify(getCacheSuggestionObj(ind)));
    console.log(activeSuggestionID);

    fetchReplies(suggestion.suggestionID);
}

//Sends AJAX request to fetch code names
function fetchCodesInfo() {
    toggleLoading();
    let uid = getParameter("userID");
    let assId = getParameter("assignmentID");
    if (uid !== null) {
        dataObj = {
            assignmentID: assId,
            teacherID: teachID,
            userID: uid
        }
    } else {
        dataObj = {
            assignmentID: assId,
            teacherID: teachID,
            courseID: getParameter("courseID")
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

function loadCodeInfoError() {
    alert("Error in loading file information");
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

//Sends AJAX request to load code
function fetchCode(id) {
    toggleLoading();
    let dataObj;
    dataObj = {
        teacherID: teachID,
        codeID: id,
        needsContent: (sessionStorage.getItem(id + codeContentSuffix) == null)
    };
    console.log(dataObj.needsContent);

    $.ajax({
        url: "/user/code_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify(dataObj),
        success: function (data) {
            toggleLoading();
            loadCode(data, dataObj);
            loadRecentSuggestion(id);
            toggleProjectView();
        },
        error: function () {
            toggleLoading();
            loadCodeError();
        }
    });
}

function getCacheSuggestionObj(ind) {
    return {
        ind: ind,
        suggestionID: suggestions[ind].suggestionID
    };
}

function loadRecentSuggestion(id) {
    var recentSuggestion = JSON.parse(localStorage.getItem(id + recentSuggestionSuffix));

    if (recentSuggestion == null) {
        console.log("There was no recent suggestion for code with ID " + id);
        return;
    }

    if (recentSuggestion.suggestionID !== suggestions[recentSuggestion.ind].suggestionID) {
        console.log("Current index references to different suggestion object, deleting recent recent suggestion from LocalStorage");
        localStorage.removeItem(id + recentSuggestionSuffix);
        return;
    }
    viewSuggestion(recentSuggestion.ind);
}

//Sends AJAX request to fetch new replies
function fetchReplies(id) {
    toggleLoading();
    console.log(id);
    $.ajax({
        url: "/user/reply_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8/json',
        data: JSON.stringify({courseID: getParameter("courseID"), teacherID: teachID, suggestionID: id}),
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

//Shortens or extends reply data
function toggleReplyContent() {
    let wrapper = $(event.target).closest('.reply-panel-wrapper');
    let fullData = $(wrapper).attr('data-full');

    if ($(wrapper).find('button').text() == 'See more') {
        $(wrapper).find('button').text('See less');
        $(wrapper).find(".reply-text").html(fullData);
    } else {
        $(wrapper).find('button').text('See more');
        $(wrapper).find(".reply-text").html(fullData.substr(0, replyContentThreshold));
    }

    console.log( $(wrapper).find(".reply-text").text().length);
}

//Draws one new reply in the suggestion panel
function drawReply(reply) {

    let newBlock = $(dashboardReplyTemplate(reply)).closest(".reply-panel-wrapper");
    console.log("newBlock");
    console.log(dashboardReplyTemplate(reply));
    $(newBlock).insertBefore("#reply-editor-wrapper");

    //Scroll to bottom
    let parDiv = document.getElementById('comment-panel-wrapper');
    parDiv.scrollTop = parDiv.scrollHeight;
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

function getParameter(name) {
    results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    return results[1] || 0;
}