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

function signOut() {
    gapi.load('client',
        {
            callback : function () {
                let id = $("meta[name='client_id']").attr("content");
                auth2 = auth2 = gapi.auth2.init({
                    client_id: id
                });

                auth2.then(function () {
                    auth2.signOut().then(function () {
                        location.href='/logout';
                    });
                });

            }
        });
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

function sendAssignments(assignmentId) {
    var courseID = getParameter("courseID");
    console.log(courseID);
    $.ajax({
        type:'POST',
        url: '/teacher-dispatcher',
        data: JSON.stringify(
            {"assignmentID" : assignmentId,
            "courseID" : courseID}
        ),
        success:function(){
            alert("Successfully downloaded assignment files.");
        },
        error:function(){
            console.log('Service call failed!');
        }
    });
}

