<%@ page import="Models.User" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="com.google.api.services.classroom.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="static Data.Constraints.USER" %>
<%@ page import="Data.Constraints" %>
<%@ page import="static Data.Constraints.COURSE_ID" %>
<%@ page import="static Data.Constraints.CLIENT_ID" %>
<%@ page import="static Data.Constraints.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>

    <meta name="client_id" content="<%=CLIENT_ID%>">

    <title>Choose Room - LINC</title>

    <!-- css -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/Styles/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/Styles/chooseRoom.css" rel="stylesheet">

    <!-- javascript -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://apis.google.com/js/platform.js?onload=start" async defer></script>
    <script src="https://apis.google.com/js/api.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/panel.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/gapi-scripts.js"></script>


    <% User user = (User) session.getAttribute(USER); %>

    <script>let userId = '<%=user.getUserId()%>';</script>

</head>
<body>
<div class="fill">
    <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
    <button id="menuBar" onclick="toggleNav()"><span></span><span></span><span></span>
    </button>
</div>
<div id="mySidenav" class="sidenav">
    <div class="sprt" aria-disabled="true" role="separator" style="user-select: none;"></div>
    <div class="sidenav-container" style="margin-top: 10px">
        <div class="sidenav-item">
            <p onclick="signOut()">Logout</p>
        </div>
    </div>
</div>
<div id="loader-wrapper">
    <div class="loader"></div>
</div>
<div id="content-wrapper">
    <h1 class="welcomeText">Welcome <%=user.getFirstName()%>! Choose classroom:</h1>

    <div class="panel" id="crs_cntr">
    </div>
</div>
</body>
<script>
    toggleLoading();
    gapi.load('client', get_classroom_list);

    function enterClasroom(teacherId, id) {
        console.log(teacherId);
        console.log(id);
        if (teacherId === userId) {
            window.location.assign("teacher-dashboard.jsp?" + '<%=Constraints.COURSE_ID%>' + "=" + id);
            return;
        }
        $.ajax({
            url: 'rooms',
            data: {courseID: id},
            type: 'GET',
            success: function (data) {
                window.location.assign(data);
            },
            error: function () {
                alert('error');
            }
        });
    }
</script>
</html>
