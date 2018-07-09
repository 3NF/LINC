function changeRole(role , userId , add , courseID){
    var thisi = event.target;
    $.ajax({
        url: '/user/change_role',
        data:{"role" : role,
              "userID": userId,
              "add" : add,
              "courseID" : courseID
        },
        type:'POST',
        success:function(data){
            var row = $(thisi).closest("tr").clone();
            $(thisi).closest("tr").remove();
            var table = $(thisi).closest("table");
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