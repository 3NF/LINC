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

function isDonwloaded(assignmentID){
    alert("You have allready uploaded the assignment");
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

function giveInSection(leader,students,nashti,inSection,courseID){
    let l = 0;
    for (let k = 0;k < leader.length; ++ k) {
        console.log(leader[k]);
        let r = l + inSection - 1;
        if (nashti > 0) {
            ++r;
            --nashti;
        }
        $.ajax({
            type: 'POST',
            url: '/user/addInSectionDispatcher',
            data: JSON.stringify(
                {
                    "leaderID": leader[k],
                    "courseID": courseID,
                    "sections": students.slice(l, r)
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

function randomSections(teacherAssistants,students,semReaders,courseID){
    /*console.log(teacherAssistants);
    console.log(students);
    console.log(semReaders);*/
    shuffle(teacherAssistants);
    shuffle(students);
    shuffle(semReaders);
    console.log("fuchuri");
    console.log(teacherAssistants);
    console.log(students);
    console.log(semReaders);
    let teacherAssistatnsCnt = teacherAssistants.length;
    let studentsCnt = students.length;
    let semReadersCnt = semReaders.length;

    let inSectionAssistant = studentsCnt/teacherAssistatnsCnt;
    let nashti = studentsCnt % teacherAssistatnsCnt;
    let inSectionSemReader = studentsCnt/teacherAssistatnsCnt;
    giveInSection(teacherAssistants,students,nashti,inSectionAssistant,courseID);
    nashti = studentsCnt%teacherAssistatnsCnt;
    giveInSection(semReaders,students,nashti,inSectionSemReader,courseID);
}
