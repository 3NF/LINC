function changeRole(userId , courseID , role){
    var etarget = event.target;
    var change;
    if ($(etarget).text() === 'Remove') {
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
            var table = $(etarget).closest("table");
            $(etarget).closest("tr").remove();
            if (change === 'add') {
                $("#removeExButton").attr("onclick","changeRole('" +userId+"','"+ courseID +"','" +role+"')");
                var button = $('#removeEx').clone();
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
                var button = $('#addEx').clone();
                row.find("td:last").empty();
                row.find("td:last").append(button);
                $("#studentsTable").append(row);
            }
            row.fadeOut();
            row.fadeIn();
        }
    });
}