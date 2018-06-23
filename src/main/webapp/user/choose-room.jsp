<%@ page import="Models.User" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="com.google.api.services.classroom.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="static Data.Constraints.USER_IN_SESSION" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="../JavaScript/script.js"></script>
<html>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        User user = (User) session.getAttribute(USER_IN_SESSION);
        List<Course> courses = GAPIManager.getActiveRooms(user);
    %>
</head>
<body>
<h1 class="welcomeText">Welcome <%=user.getFirstName()%>! Choose ClassRoom To Enter:</h1>


<div class="panel">

    <%for (Course course : courses) {%>

    <td>
        <div class="classRoom" onclick=<%="enterClasroom(" + course.getId() + ")"%>>
            <h3><%=course.getName()%>
            </h3>
        </div>
    </td>

    <%}%>
</div>

</body>
</html>
