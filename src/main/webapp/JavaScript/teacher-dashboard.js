$(document).ready(function(){
    $('[data-toggle="popover"]').popover({
        html:true,
        placement : 'bottom',
        content: function() {
            var content = $(this).attr("data-popover-content");
            return $(content).html();
        },
        trigger : 'click'
    });
    $('[data-toggle="studentPopover"]').popover({
        html:true,
        placement : 'bottom',
        content: function() {
            var content = $(this).attr("data-popover-content");
            return $(content).html();
        },
        trigger : 'click'
    });
});

function changeRole(role , userId , add){
    console.log("me var barni");
    $.ajax({
        url: '/user/change_role',
        data:{"role" : role,
              "userID": userId,
              "add" : add
        },
        type:'POST',
        success:function(data){
            location.reload();
        },
        error:function(data){
            console.log('Service call failed!');
        }
    });
}