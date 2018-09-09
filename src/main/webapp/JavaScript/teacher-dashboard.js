// This function is for changing user's role
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
                $("#removeExButton").attr("onclick",`changeRole('${userId}', '${courseID}', ${role})`);
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
                $("#addExButtonSR").attr("onclick", `changeRole('${userId}', '${courseID}', 'SeminarReader')`);
                $("#addExButtonTA").attr("onclick", `changeRole('${userId}', '${courseID}', 'TeacherAssistant')`);
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
    let progressBarDiv = $('#fetch-assignment-div-wrapper');

    progressBar.set(0);
    progressBar.setText("");
    if (progressBarDiv.is(":visible")) {
        $(progressBarDiv).fadeOut();
    } else {
        $(progressBarDiv).fadeIn();
    }
}

var progressBar;
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

function downloadAssignment (assignmentID) {

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
        if (progressData === 1) {
            progressBar.setText("Saving in database!")
        } else {
            //progressBar.setText(progressData*100 + "%");
        }
    });
}

function appointInSections(leaders, students, rem, inSection, courseID) {
    let l = 0;
    for (let k = 0; k < leaders.length; ++k) {
        let r = l + inSection - 1;
        if (rem > 0) {
            ++r;
            --rem;
        }
        console.log (leaders.length + " " + students.length + " " + " " +  courseID);
        $.ajax({
            type: 'POST',
            url: '/user/add_in_section_servlet',
            data: JSON.stringify(
                {
                    "leaderID": leaders[k],
                    "courseID": courseID,
                    "sections": students.slice(l, r + 1)
                }
            ),
            error: function () {
                console.log('Service call failed!');
            }
        });
        l = r + 1;
    }
}

/**
 * Shuffles array in place. ES6 version
 * @param {Array} a items An array containing the items.
 */
function shuffle(a) {
    for (let i = a.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [a[i], a[j]] = [a[j], a[i]];
    }
    return a;
}

function randomizeSections() {
    let assistants_cln = JSON.parse(JSON.stringify(assistants));
    let students_cln = JSON.parse(JSON.stringify(students));
    let seminarReaders_cln = JSON.parse(JSON.stringify(seminarReaders));

    shuffle(assistants_cln);
    shuffle(students_cln);
    shuffle(seminarReaders_cln);

    let teacherAssistantCnt = assistants_cln.length;
    let studentsCnt = students_cln.length;
    let semReadersCnt = seminarReaders_cln.length;

    let inSectionAssistant = studentsCnt / teacherAssistantCnt;
    let rem = studentsCnt % teacherAssistantCnt;
    let inSectionSemReader = studentsCnt / semReadersCnt;

    console.log (teacherAssistantCnt);
    console.log (studentsCnt);
    console.log (semReadersCnt);
    appointInSections(assistants_cln.map(student => student.userId), students_cln.map(student => student.userId), rem, inSectionAssistant, courseID);

    rem = studentsCnt % teacherAssistantCnt;

    appointInSections(seminarReaders_cln.map(student => student.userId), students_cln.map(student => student.userId), rem, inSectionSemReader, courseID);

    alert("Random-Fucking-ised!");
}


function isDownloaded() {
    alert("You have already downloaded the assignment");
}