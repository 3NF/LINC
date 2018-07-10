let isVisible = false;

$(document).ready(function () {
    console.log("123");
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function(){
        console.log("11");
        location.assign("/user/choose-room.jsp");
    });

    console.log($(".sidenav-container").first().html());//.attr("onclick"));
});

function togleNav() {
    if (!isVisible)
        $("#mySidenav")[0].style.width = "250px";
    else
        $("#mySidenav")[0].style.width = "0";
    isVisible = !isVisible;
}

function start() {
    gapi.auth2.init({
        client_id: '108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com'
    });
    gapi.auth2.getAuthInstance().signOut();

}

function signOut() {
    gapi.load('client', start);
    location.href='../logout';
}

function getParameter (name) {
    try {
        results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return results[1] || 0;
    } catch (e) {
        return null;
    }
}

function getAssignment(assignmentId) {
    console.log(location.href);
    console.log(getParameter("assignmentID"));
    if (getParameter("assignmentID") != null) {
        location.href = location.href + "assignmentID=" + assignmentId;
    }
}

