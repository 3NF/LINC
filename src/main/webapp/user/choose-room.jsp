<%@ page import="Models.User" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="com.google.api.services.classroom.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page import="static Data.Constraints.USER" %>
<%@ page import="Data.Constraints" %>
<%@ page import="static Data.Constraints.COURSE_ID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="../Styles/assets/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<html>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        User user = (User) session.getAttribute(USER);
        List<Course> courses = GAPIManager.getInstance().getActiveRooms(user);
        System.out.println(user.getUserId());
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
<script>
    function enterClasroom(id) {
        $.ajax({
            url: 'rooms',
            data: {courseID : id},
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
