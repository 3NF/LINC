function changeRole(userId , courseID , role){
    console.log (userId);
    console.log (courseID);
    console.log (role);
    let etarget = event.target;
    let change;
    if ($(etarget).text() === 'Remove') {
        change = 'remove';
    } else {
        change = 'add';
    }
    console.log ("abc " + userId);
    $.ajax({
        url: '/user/change_role',
        data:{"role" : role,
              "userID": userId,
              "change" : change,
              "courseID" : courseID
        },
        type:'POST',
        success:function(){
            let row = $(etarget).closest("tr").clone();
            $(etarget).closest("tr").remove();
            if (change === 'add') {
                $("#removeExButton").attr("onclick","changeRole('" +userId+"','"+ courseID +"','" +role+"')");
                let button = $('#removeEx').clone();
                row.find("td:last").empty();
                row.find("td:last").append(button);
                if (role === 'TeacherAssistant') {
                    $("#teacherAssTable").append(row);
                }
                else {
                    $("#semReadersTable").append(row);
                }
            }else{
                $("#addExButtonSR").attr("onclick","changeRole('" +userId+"','"+ courseID +"','SeminarReader')");
                $("#addExButtonTA").attr("onclick","changeRole('" +userId+"','"+ courseID +"','TeacherAssistant')");
                let button = $('#addEx').clone();
                row.find("td:last").empty();
                row.find("td:last").append(button);
                $("#studentsTable").append(row);
            }
            row.fadeOut();
            row.fadeIn();
        }
    });
}

function toggleProgressBar () {
    var progressBarDiv = $('#fetch-assignment-div-wrapper');

    progressBar.set(0);
    progressBar.setText("");
    if (progressBarDiv.is(":visible")) {
        $(progressBarDiv).fadeOut();
    } else {
        $(progressBarDiv).fadeIn();
    }
}

var proegressBar;

function isDownloaded() {
    alert("You have already downloaded the assignment");
}

$(document).ready (function () {
    $("#fetch-assignment-div-wrapper").hide();
    progressBar = new ProgressBar.Circle('#fetch-assignment-div', {
        color: '#aaa',
        strokeWidth: 3,
        trailWidth: 1,
        step: function(state, circle) {
            var value = Math.round(circle.value() * 100);
            if (value === 0) {
                circle.setText('');
            } else {
                circle.setText(value);
            }

        }
    });
});

var assignmentSocket = new WebSocket("ws://" + document.location.host + "/download_assignment");

assignmentSocket.onopen = function () {
    console.log ("WebSocket have connected to server endpoint");
};

function sendAssignmentsNew (assignmentID) {
    // assignmentSocket.activeEl = $(event.target).parent();
    // console.log(assignmentSocket.activeEl);

    toggleProgressBar();
    var dataObj = {
        assignmentID: assignmentID,
        courseID: courseID
    };

    assignmentSocket.send(JSON.stringify(dataObj));
}

assignmentSocket.onmessage = function (event) {
    console.log ("Message received");

    var progressData = event.data;
    console.log (progressData);

    if (progressData !== "Completed") {
        updateProgress (progressBar, progressData);
    } else {
        downloadCompleted ();
    }
};

function downloadCompleted () {
    progressBar.setText("Completed, Click to hide!");
    // $(assignmentSocket.activeEl).css("color", "green");
    // $(assignmentSocket.activeEl).unbind();
    // $(assignmentSocket.activeEl).click(isDownloaded);
}

function updateProgress(progressBar, progressData) {
    progressBar.animate (progressData, {
        duration: 500,
        easing: 'easeInOut'
    }, function () {
        if (progressData == 1) {
            progressBar.setText("Saving in database!")
        } else {
            //progressBar.setText(progressData*100 + "%");
        }
    });
}

