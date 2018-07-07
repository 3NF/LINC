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