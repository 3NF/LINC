$(document).ready(function () {
    console.log("1111111");
    $(".fill").css("display", "block");
    $(".sidenav").css("display", "block");

    $('#goHome').on("click", function(){
        console.log("11");
        location.assign("/user/choose-room.jsp");
    });

    console.log($(".sidenav-container").first().html());//.attr("onclick"));
});