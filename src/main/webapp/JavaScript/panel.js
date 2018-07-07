let isVisible = false;

$(document).ready(function () {
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");
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
}
