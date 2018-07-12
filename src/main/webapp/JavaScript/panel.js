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
    console.log(location.href);
    console.log(getParameter("assignmentID"));
    if (getParameter("assignmentID") != null) {
        location.href = location.href + "assignmentID=" + assignmentId;
    } else {
        assignmentID = assignmentId;
    }
}

function isDownloaded(assignmentID){
    alert("You have allready uploaded the assignment");
}

function changeParameter(paramName, paramVal) {
    const oldLink = location.href;
    const chunks = oldLink.split("&");
    let newLink = chunks[0] + "&";
    for (let i = 1; i < chunks.length; i ++) {
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

function giveInSection(leaders, students, rem, inSection, courseID){
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
            success: function () {
            },
            error: function () {
                console.log('Service call failed!');
            }
        });
        l = r + 1;
    }
}

function randomSections(teacherAssistants, students, semReaders, courseID){

    shuffle(teacherAssistants);
    shuffle(students);
    shuffle(semReaders);

    let teacherAssistantCnt = teacherAssistants.length;
    let studentsCnt = students.length;
    let semReadersCnt = semReaders.length;

    let inSectionAssistant = studentsCnt / teacherAssistantCnt;
    let rem = studentsCnt % teacherAssistantCnt;
    let inSectionSemReader = studentsCnt / semReadersCnt;

    giveInSection(teacherAssistants,students,rem,inSectionAssistant,courseID);

    rem = studentsCnt%teacherAssistantCnt;

    giveInSection(semReaders,students,rem,inSectionSemReader,courseID);
    alert("now section,seminarers leaders has their students");
}
