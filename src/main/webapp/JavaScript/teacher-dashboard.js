function changeRole(role , userId , add , courseID){
    $.ajax({
        url: '/user/change_role',
        data:{"role" : role,
              "userID": userId,
              "add" : add,
              "courseID" : courseID
        },
        type:'POST',
        success:function(data){
            var row = $(this).closest("tr");
            var table = $(this).closest("table");
            table.detach();
            row.detach();
            if (role==='TeacherAssistant') {
                $("#teacherAssTable").append('<tr>' + row + '</tr>');
                console.log("ki");
            }
            else {
                $("#semReadersTable").append(row);
            }
            row.fadeOut();
            row.fadeIn();
            console.log("YAY");
            console.log("NNNNAY");
        },
        error:function(data){
            console.log('Service call failed!');
        }
    });
}