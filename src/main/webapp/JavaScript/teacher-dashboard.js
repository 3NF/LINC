function changeRole(userId , courseID , role){
    var etarget = event.target;
    var change;
    if ($(etarget).text() == 'Remove') {
        change = 'remove';
    } else {
        change = 'add';
    }
    $.ajax({
        url: '/user/change_role',
        data:{"role" : role,
              "userID": userId,
              "change" : change,
              "courseID" : courseID
        },
        type:'POST',
        success:function(){
            var row = $(etarget).closest("tr").clone();
            $(etarget).closest("tr").remove();
            var table = $(etarget).closest("table");
            if (change === 'add') {
                var button = $('#removeEx').clone();
                button.childNodes[0].childNodes[0].attr("onclick" , param);
                row.childNodes[4].childNodes[0] = button;
                if (role === 'TeacherAssistant') {
                    $("#teacherAssTable").append(row);
                }
                else {
                    $("#semReadersTable").append(row);
                }
            }else{
                var button = $('#addEx').clone();
                button.childNodes[0].childNodes[0].attr("onclick" , param);
                button.childNodes[1].childNodes[0].attr("onclick" , param);
                row.childNodes[4].childNodes[0] = button;
                $("#studentsTable").append(row);
            }
            row.fadeOut();
            row.fadeIn();
        },
        error:function(){
            console.log('Service call failed!');
        }
    });
}