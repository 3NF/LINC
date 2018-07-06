let isVisible = false;

$(document).ready(function () {
    console.log("123");

    let dvSlider = "<div id=\"mySidenav\" class=\"sidenav\"\> <a class=\"logout\" onclick=\"signOut()\">Logout</a></div>";
    let dvPn = `<div class="fill"> <div style="cursor:pointer" onclick="togleNav()"> <img src=${userProfilePicture} class="img-circle" alt="Cinque Terre" id="user-panel-img" > </div> <div id = "menuBar" onclick="togleNav()"> <span class="glyphicon">&#xe236;</span> </div></div>`;
    $("body").prepend(dvSlider);
    $("body").prepend(dvPn);
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
    $.post()
}
