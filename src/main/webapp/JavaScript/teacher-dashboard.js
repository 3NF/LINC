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