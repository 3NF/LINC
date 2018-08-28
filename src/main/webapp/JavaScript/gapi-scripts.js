/**
 * From this method you can ask classroom api
 * @param path  HTTP request path for Classroom Rest api
 * @param sucFunc  Function which calls when api returns response
 */
function gapi_query(path, sucFunc) {
    gapi.client.init({
        'apiKey': 'AIzaSyBKiQttlC5rUqexQiZgXlP2Zmhod5QZJhA',
        client_id: '108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com',
        scope: "profile email https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly " +
        "https://www.googleapis.com/auth/classroom.coursework.students.readonly https://www.googleapis.com/auth/classroom.coursework.students " +
        "https://www.googleapis.com/auth/classroom.rosters " +
        "https://www.googleapis.com/auth/classroom.profile.photos " +
        "https://www.googleapis.com/auth/classroom.profile.emails  " +
        "https://www.googleapis.com/auth/drive"
    }).then(function () {
        return gapi.client.request({
            'path': path,
        })
    }).then(function (response) {
        sucFunc(response);
    }, function (reason) {
        console.log(reason);
        location.href = "/error-page.jsp";
    });
}


function get_classroom_list() {
    toggleLoading();
    gapi_query('https://classroom.googleapis.com/v1/courses?courseStates=ACTIVE', function (response) {
        let lst = response.result.courses;
        for (let i = 0; i < lst.length; i++) {
            let dv = `<div class="classroom"  onclick=enterClasroom('${lst[i].ownerId}','${lst[i].id}')><img src="/Images/Logo.svg"><h3 class="classroom-name">${lst[i].name}</h3></div>`;
            $("#crs_cntr").append(dv);
        }
        toggleLoading();
    });
}

function get_students() {
    gapi_query('https://classroom.googleapis.com/v1/courses/' + courseID + '/students', function (response) {
        students = response.result.students;
        seminarReaders = students.filter(student => seminarReaderIds.includes(student.userId));
        assistants = students.filter(student => assistantIds.includes(student.userId));
        students = students.filter(student => !(assistants.includes(student) || seminarReaders.includes(student)));


        document.getElementById("semReadersTable1").innerHTML = seminarReaders.map(instructor_teacher_page_template).join('');
        document.getElementById("teacherAssTable1").innerHTML = assistants.map(instructor_teacher_page_template).join('');
        document.getElementById("studentsTable1").innerHTML = students.map(stundet_teacher_page_template).join('');
    });
}