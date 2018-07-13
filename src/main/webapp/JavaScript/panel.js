let manuIsVisible = false;

function toggleLoading() {
    $("#loader-wrapper").fadeToggle();
}

const gradeMap = {
    '0': '0',
    'Plus': 'Plus',
    'Check': 'Check',
    'Minus': 'Minus',
    'none': 'Not graded',
    'PlusPlus': 'Plus Plus',
    'CheckPlus': 'Check Plus',
    'CheckMinus': 'Check Minus',
    'MinusMinus': 'Minus Minus'
};

$(document).ready(function () {
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function () {
        location.assign("/user/choose-room.jsp");
    });

    document.getElementById('menuBar').addEventListener('click', function () {
        if (this.className === 'on') this.classList.remove('on');
        else this.classList.add('on');
    });
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
            callback: function () {
                let id = $("meta[name='client_id']").attr("content");
                auth2 = auth2 = gapi.auth2.init({
                    client_id: id
                });

                auth2.then(function () {
                    auth2.signOut().then(function () {
                        location.href = '/logout';
                    });
                });

            }
        });
}

function get_classroom_list() {
    // 2. Initialize the JavaScript client library.
    gapi.client.init({
        'apiKey': 'AIzaSyBKiQttlC5rUqexQiZgXlP2Zmhod5QZJhA',
        // clientId and scope are optional if auth is not required.
        client_id: '108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com',
        scope: "profile email https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly " +
        "https://www.googleapis.com/auth/classroom.coursework.students.readonly https://www.googleapis.com/auth/classroom.coursework.students " +
        "https://www.googleapis.com/auth/classroom.rosters " +
        "https://www.googleapis.com/auth/classroom.profile.photos " +
        "https://www.googleapis.com/auth/classroom.profile.emails  " +
        "https://www.googleapis.com/auth/drive"
    }).then(function () {
        // 3. Initialize and make the API request.
        return gapi.client.request({
            'path': 'https://classroom.googleapis.com/v1/courses?courseStates=ACTIVE',
        })
    }).then(function (response) {
        let lst = response.result.courses;
        console.log(lst);
        for (let i = 0; i < lst.length; i++) {

            let dv = `<div class="classRoom"  onclick=enterClasroom('${lst[i].ownerId}','${lst[i].id}')><h3>${lst[i].name}</h3></div>`;
            $("#crs_cntr").append(dv);
        }
    }, function (reason) {
        console.log('Error: ' + reason.result.error.message);
    });
}

function test_classroom_api() {
    gapi.client.request({
        'path': 'https://people.googleapis.com/v1/people/me?requestMask.includeField=person.names',
    }).then(function (response) {
        console.log(response.result);
    }, function (reason) {
        console.log('Error: ' + reason.result.error.message);
    });
}

function getParameter(name) {
    try {
        let result = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        return result[1] || 0;
    } catch (e) {
        return null;
    }
}

function getAssignment(assignmentId) {
    if (getParameter("assignmentID") != null) {
        location.href = changeParameter("assignmentID", assignmentId);
    } else {
        assignmentID = assignmentId;
    }

}

function isDownloaded(assignmentID) {
    alert("You have allready uploaded the assignment");
}

function changeParameter(paramName, paramVal) {
    const oldLink = location.href;
    let ind_of_param = oldLink.indexOf(paramName);
    let old_value_start = oldLink.indexOf("=", ind_of_param + 1) + 1;
    let old_value_end = oldLink.indexOf("&", old_value_start + 1);
    if (old_value_end === -1) old_value_end = oldLink.length;
    return oldLink.substr(0, old_value_start) + paramVal + oldLink.substr(old_value_end);
}

function sendAssignments(assignmentId) {
    const courseID = getParameter("courseID");
    if (window.confirm("Do You want to download assignments?")) {
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

function giveInSection(leaders, students, rem, inSection, courseID) {
    let l = 0;
    for (let k = 0; k < leaders.length; ++k) {
        let r = l + inSection - 1;
        if (rem > 0) {
            ++r;
            --rem;
        }
        $.ajax({
            type: 'POST',
            url: '/user/addInSectionServlet',
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

function randomSections(teacherAssistants, students, semReaders, courseID) {

    shuffle(teacherAssistants);
    shuffle(students);
    shuffle(semReaders);

    let teacherAssistantCnt = teacherAssistants.length;
    let studentsCnt = students.length;
    let semReadersCnt = semReaders.length;

    let inSectionAssistant = studentsCnt / teacherAssistantCnt;
    let rem = studentsCnt % teacherAssistantCnt;
    let inSectionSemReader = studentsCnt / semReadersCnt;

    giveInSection(teacherAssistants, students, rem, inSectionAssistant, courseID);

    rem = studentsCnt % teacherAssistantCnt;

    giveInSection(semReaders, students, rem, inSectionSemReader, courseID);
    alert("now section,seminarers leaders has their students");
}

function mouseOver(grade) {
    var etarget = event.target;
    var changedGrade = gradeMap[grade];
    etarget.innerHTML = changedGrade;
}