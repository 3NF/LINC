var codeMirror;

var suggestionEditor;
var replyEditor;

//Array for storing suggestion objects
var suggestions = [];
//HTML line element array
var lines = [];

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
function loadCode (data, textStaus, jQxhr) {
    var receivedData = $.parseJSON(data);
    suggestions = receivedData.suggestions;

    console.log (receivedData);
    codeMirror.setValue(receivedData.code);

    mapCodeLines ();
    placeSuggestions ();
}

/*
    Places suggestions in appropriate line intervals
 */
function placeSuggestions () {
    for (var i = 0; i < suggestions.length; i ++) {

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
        for (var lineIterator = start; lineIterator < end+1; lineIterator ++) {
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
function viewSuggestion (eventHandler) {
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
        console.log("abc");
        $("#notification-div").hide();
        $("#comment-panel").show();
        $("#reply-editor-wrapper").show();
    }

    //Load suggestion data
    var suggestion = suggestions[eventHandler.data];
    $("#comment-content").css("border-right-color", suggestion.color);
    $("#comment-text").html(suggestion.content);
    $("#comment-user-name").html(suggestion.user);
    $("#comment-profile-picture").attr("src", suggestion.imgSrc);
    $("#comment-date").html(suggestion.timeStamp);

    fetchReplies (suggestion.suggestionID);
}

/*
    Loads new code in CodeMirror editor
 */
function navbarOnClick () {
    console.log("There was a Click!");

    //Change html elements' styles
    $("#navbar").find(".active").removeClass("active");
    $(event.target).parent().attr("class", "active");

    //Get name of code
    var name = $(event.target).text();

    fetchCode(name);
}

//Writes loading message
function showLoading (fileName) {
    $("#code-content").html("Loading " + fileName + "...");
}

//Sends AJAX request to load code
function fetchCode (name) {
    showLoading(name);
    $.ajax({
        url: "/user/code_dispatcher",
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify({codeName: name}),
        success: function( data, textStatus, jQxhr ){
            loadCode(data, textStatus, jQxhr);
        },
        error: function (data, textStatus, jQxhr) {
            loadCodeError(data, textStatus, jQxhr);
        }});
}



//Sends AJAX request to fetch new replies
function fetchReplies (id) {
    //showSuggestionsLoading();
    console.log(id);
    $.ajax({
        url: "/user/reply_dispatcher",
        method: "POST",
        contentType: 'application/json',
        data: JSON.stringify({suggestionID: id}),
        success: function( data, textStatus, jQxhr ){
            loadReplies(data, textStatus, jQxhr);
        },
        error: function (data, textStatus, jQxhr) {
            loadReplyError(data, textStatus, jQxhr);
        }});
}

//AJAX successful callback for receiving reply data
function loadReplies (data) {
    console.log(data.length);
    clearReplies();
    for (var i = 0; i < data.length; i ++) {
        console.log(data[i]);
        drawReply(data[i]);
    }
}

//AJAX error callback for receiving reply data
function loadReplyError (data) {
    console.log ("Reply error");
}

//Clears all current replies
function clearReplies () {
    $(".reply-panel-wrapper").remove();
}

//Draws one new reply in the suggestion panel
function drawReply (reply) {
    var newBlock = $(replyBlock).closest(".reply-panel-wrapper");
    $(newBlock).find(".reply-user-name").html(reply.user);
    $(newBlock).find(".reply-profile-picture").attr("src", reply.imgSrc);
    $(newBlock).find(".reply-text").html(reply.content);
    $(newBlock).find(".reply-date").html(reply.timeStamp);

    $(newBlock).insertBefore("#reply-editor-wrapper");
}

/*
    Maps appropriate code lines for intervals
 */
function mapCodeLines() {
    $(".CodeMirror-linenumber").each(function( index ) {
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
function loadCodeError (data, textStatus, jQxhr) {
    codeMirror.setValue("Couldn't find requested file!");
}

/*
    Creates CodeMirror and Bootstrap Markdown editors
 */
function onLoad () {
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
        onShow: function(e) {
            suggestionEditor = e;
        }
    });

    //Create Bootstrap Markdown editor for reply editor
    $("#reply-editor-content").markdown({
        autofocus: true,
        saveable: true,
        onShow: function(e) {
            replyEditor = e;
        }
    });
    /*
    Gets initial data for already
    activated code
    */
    var name = $("#navbar").find(".active").find("a").text();
    fetchCode(name);
}

function onSubmit () {
    console.log(replyEditor.parseContent());
}