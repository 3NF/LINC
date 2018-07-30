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
    document.location.href = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/logout";
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
    alert("You have already uploaded the assignment");
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

function randomSections() {
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

    giveInSection(assistants_cln.map(student => student.userId), students_cln.map(student => student.userId), rem, inSectionAssistant, courseID);

    rem = studentsCnt % teacherAssistantCnt;

    giveInSection(seminarReaders_cln.map(student => student.userId), students_cln.map(student => student.userId), rem, inSectionSemReader, courseID);

    alert("now section,seminarers leaders has their students");
}

function mouseOver(grade) {
    let etarget = event.target;
    etarget.innerHTML = gradeMap[grade];
}