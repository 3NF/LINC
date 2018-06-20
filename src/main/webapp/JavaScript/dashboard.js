var navbarOnClick = function () {
    console.log("There was a Click!");

    //Change html elements' styles
    $("#navbar").find(".active").removeClass("active");
    $(event.target).parent().attr("class", "active");

    //Get name of code
    var name = $(event.target).text();

    fetchCode(name);
};

//Writes loading message
var showLoading = function (fileName) {
    $("#code-content").html("Loading " + fileName + "...");
};

//Sends AJAX request
var fetchCode = function (name) {
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
            loadError(data, textStatus, jQxhr);
        }});
};

//AJAX successful response callback
var loadCode = function (data, textStaus, jQxhr) {
    editor.setValue(data);
};

var editor;

//AJAX error response callback
var loadError = function (data, textStatus, jQxhr) {
    editor.setValue("Couldn't find requested file!");
};

var onLoad = function () {
    /*
        Gets initial data for already
        activated code
     */
    var name = $("#navbar").find(".active").find("a").text();
    fetchCode(name);


    editor = CodeMirror.fromTextArea($("#code-content").get(0), {
        lineNumbers: true,
        mode: "text/x-c++src"
    });

    editor.setSize("100%", "100%");
};