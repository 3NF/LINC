<%@ page import="Data.Constraints" %>
<%@ page import="Models.User" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.classroom.model.Teacher" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        User user = (User) request.getSession().getAttribute(Constraints.USER);
        String courseId = request.getParameter(Constraints.ROOM_ID);

        List<Teacher> teachers = GAPIManager.getInstance().getTeachers(user, courseId);

    %>

    <title>Title</title>


    <%
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getProfile().getName());
        }
    %>


</head>
<body>

</body>
</html>
