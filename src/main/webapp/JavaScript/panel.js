let manuIsVisible = false;

$(document).ready(function () {
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function(){
        console.log("11");
        location.assign("/user/choose-room.jsp");
    });

    document.getElementById('menuBar').addEventListener('click', function() {
        if (this.className === 'on') this.classList.remove('on');
        else this.classList.add('on');
    });
    console.log($(".sidenav-container").first().html());//.attr("onclick"));
});


function toggleNav() {
    if (!manuIsVisible)
        $("#mySidenav")[0].style.width = "250px";
    else
        $("#mySidenav")[0].style.width = "0";
    manuIsVisible = !manuIsVisible;
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
    if (getParameter("assignmentID") != null) {
        location.href = changeParameter ("assignmentID", assignmentId);
    } else {
        assignmentID = assignmentId;
    }
}

function changeParameter(paramName, paramVal) {
    var oldLink = location.href;
    var chunks = oldLink.split("&");
    var newLink = chunks[0] + "&";
    for (var i = 1; i < chunks.length; i ++) {
        if (chunks[i] === paramName + "=") {
            chunks[i+1] = paramVal;
        }
        newLink += (chunks[i] + "&");
    }
    return newLink.substring(0, newLink.length - 1);
}

function sendAssignments(assignmentId) {
    const courseID = getParameter("courseID");
    if (window.confirm("Do You want to download assignments?")) {
        console.log(courseID);
        $.ajax({
            type: 'POST',
            url: '/teacher-dispatcher',
            data: JSON.stringify(
                {
                    "assignmentID": assignmentId,
                    "courseID": courseID
                }
            ),
            success: function () {
                alert("Successfully downloaded assignment files.");
            },
            error: function () {
                console.log('Service call failed!');
            }
        });
    }
}

