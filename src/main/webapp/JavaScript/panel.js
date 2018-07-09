let isVisible = false;

$(document).ready(function () {
    console.log("123");
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function(){
        console.log("11");
        location.assign("/user/choose-room.jsp");
    });
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

/* TODO ლუკა, ესაინმენთის ფაილების შეცვლა როცა პანელზე დაეჭეირაბა კონკრეტულ დავალებას*/
function getAssignment(assignmentId) {
    console.log(assignmentId);
}