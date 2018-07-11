var codeMirror;

var suggestionEditor;
var replyEditor;

var codeIDs;
//Array for storing suggestion objects
var suggestions = [];
//HTML line element array
var lines = [];

var codeInfo = [];

var activeSuggestionID = -1;
var activeCodeFileID = -1;

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


function loadFileTree(paths) {
    let baseNode = document.getElementById("jstree_demo_div");
    let rootUl = document.createElement("ul");

    baseNode.appendChild(rootUl);

    paths.forEach(function (pIndex, path) {
        let paths = path.split('/');
        paths.forEach(function (nIndex, node) {
            let newLi = document.createElement("li");
            rootUl.appendChild(newLi);
            rootUl = rootUl.childNodes[0];
        })
    })
}


function AddChilds(baseNode, nodes) {
    if (nodes.count == 0) return;

    let elem = nodes[0];

    let exelem = $(container).children('[data-directory]')
}

/*
    Places suggestions in appropriate line intervals
 */
function placeSuggestions() {
    for (var i = 0; i < suggestions.length; i++) {

        var color;
        var start = suggestions[i].startInd;
        var end = suggestions[i].endInd;
        if (suggestions[i].type === "Error") {
            suggestions[i].color = errorColor;
        } else {
            suggestions[i].color = warningColor;
        }

        /*
            Edit suggestion lines
         */
        for (var lineIterator = start; lineIterator < end + 1; lineIterator++) {
            $(lines[lineIterator]).css("background-color", suggestions[i].color);
            $(lines[lineIterator]).css("color", "#ffffff");
            $(lines[lineIterator]).unbind("click");
            $(lines[lineIterator]).click(i, viewSuggestion);
        }
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
    if ($("#notification-div").is(":visible")) {
        $("#notification-div").hide();
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
    $.ajax({
        url: "/user/code_dispatcher",
        method: "POST",
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({courseID: getParameter("courseID"), assignmentID: getParameter("assignmentID")}),
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
        return (a < b ? -1 : (a > b ? 1 : 0))
    });
    addCodes();
    //getFirstCode ();
}

function addCodes() {
    let ul = document.createElement("ul");
    $('#jstree_demo_div')[0].appendChild(ul);
    draw_view_rec(ul, 0, codeInfo.length - 1);
    build_project_view();
    /*for (var i = 0; i < codeInfo.length; i++) {
        console.log(codeInfo[i]);
        var newElement = $("#navbar-element").clone(true);
        $(newElement).removeAttr("id");
        $(newElement).find("a").html(codeInfo[i].value);
        $(newElement).show();
        $(newElement).appendTo("#navbar");
    }
    $("#navbar-element").remove();
    $("#navbar li:first-child").addClass("active");*/
}

function getFirstCode() {
    var name = $("#navbar").find(".active").find("a").text();
    fetchCode(name);
}

function loadCodeInfoError() {
    alert("Error in loading file information");
}

//Sends AJAX request to load code
function fetchCode(name) {
    toggleLoading();
    id = getCodeInd(name);
    var dataObj;
    console.log("uid " + uid);
    if (uid !== null) {
        dataObj = {
            courseID: getParameter("courseID"),
            codeID: id,
            userID: uid
        }
    } else {
        dataObj = {
            courseID: getParameter("courseID"),
            codeID: id
        }
    }

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

function getCodeInd(name) {
    for (var i = 0; i < codeInfo.length; i++) {
        if (codeInfo[i].name == name) {
            return codeInfo[i].id;
        }
    }
    return undefined;
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
    for (var i = 0; i < data.length; i++) {
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
    codeMirror.setSize("100%", "100%");

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
    console.log(replyEditor.parseContent());
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