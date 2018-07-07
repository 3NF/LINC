<%@ page import="Data.Constraints" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="Models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        User user = (User) request.getSession().getAttribute(Constraints.USER);
        String courseId = request.getParameter(Constraints.COURSE_ID);

        if (UserDAO.getRoleByCourse(user, courseId) != UserDAO.Role.TeacherAssistant) {
            response.sendRedirect("choose-room.jsp");
            return;
        }

    %>

    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>

    <title>Title</title>

</head>
<body>
<div class="fill">
    <div style="cursor:pointer" onclick="togleNav()">
        <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
    </div>
    <div id="menuBar" onclick="togleNav()">
        <span class="glyphicon">&#xe236;</span>
    </div>
</div>
<div id="mySidenav" class="sidenav">
    <a class="logout" href='../logout' onclick="signOut()">Logout</a>
</div>
</body>
</html>
