

function clickClassroom(id){
    location.href = "dashboard.jsp?id=id";
}

function enterClasroom(id) {
    $.ajax({
        url: 'rooms',
        data: {courseId : id},
        type: 'GET',
        success: function (data) {
            window.location = data;
        },
        error: function () {
            alert('error');
        }
    });
}