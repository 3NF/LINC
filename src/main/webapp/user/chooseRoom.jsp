<%@ page import="Models.User" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="com.google.api.services.classroom.model.Course" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<html>
<style>
    .dodo {
        border-radius: 7px;
        border-color: black;
        width: 272px;
        height: 200px;
        background-image: url(https://lh6.googleusercontent.com/-n7VYF50Z47M/VN0onnOtbdI/AAAAAAAAAYQ/lwyObtT3Xfo/w984-h209-no/153_chalkboard_bluegrey.jpg);
        background-repeat: no-repeat;
        background-color: grey;
        background-size: 400px;
    }
</style>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        if (!Validate.isLogged(session)) {
            response.sendRedirect("../loginPage.jsp");
            return;
        }
        //for testing
        User user = (User) session.getAttribute("user");
        List<Course> courses = GAPIManager.getUserRooms(user);
    %>
</head>
<body>
<h3>Welcome <%=user.getFirstName()%>! Choose ClassRoom To Enter:</h3>


<table class="table">
    <tbody>
    <tr>
        <%
            int element = 0;
            for (Course course : courses) {
            if (element % 4 == 0 && element != 0) {
        %></tr>
    <tr><%
        }
        ++element;%>
        <td>
            <div class="dodo" onclick="enterClasroom()" onmouseenter="myFunction(this)" onmouseleave="second(this)">
                <h3 align="center" style="padding-top: 100px;"><%=course.getName()%>
                </h3>
            </div>
        </td>

        <%}%>
    </tr>
    </tbody>
</table>

</body>

<script>
    function enterClasroom() {
        alert("ggs");
    }

    function myFunction(r) {
        //r.style.backgroundColor="red";
        r.style.width = '300px';
        r.style.height = '250px';
    }

    function second(r) {
        r.style.width = '272px';
        r.style.height = '200px';
    }
</script>
</html>
