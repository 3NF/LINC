<%@ page import="Data.Constraints" %>
<%@ page import="Models.User" %>
<%@ page import="Database.GAPIManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        User user = (User) request.getSession().getAttribute(Constraints.USER_IN_SESSION);
        String courseId = request.getParameter(Constraints.COURSE_ID);

        if (GAPIManager.getRoleByCourse(user, courseId) != GAPIManager.Role.TeacherAssistant) {
            response.sendRedirect("choose-room.jsp");
            return;
        }

    %>

    <title>Title</title>

</head>
<body>

</body>
</html>
