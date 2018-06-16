<%@ page import="Models.User" %>
<%@ page import="Models.BasicRoomInfo" %>
<%@ page import="com.google.api.services.classroom.model.Course" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        if (!Validate.isLogged(session)) {
            response.sendRedirect("../loginPage.jsp");
            return;
        }
        //for testing
        User user = new User("g.chxikvadze.14.12@gmail.com", "Giorgi", "Chkhikvadze", "g.chxikvadze.14.12@gmail.com", "er", "er", "er");
        BasicRoomInfo[] roomInfos = {new BasicRoomInfo("ChudoSchool", "ChudoSchool", "Giorgi Chkhivadze", "er"), new BasicRoomInfo("BezhoyStudy", "BezhoyStudy", "Davit Bezhanishvili", "er")};
    %>
</head>

<style>
    .dodo {
        border-radius: 7px;
        border-color: black;
        background-image: url(https://lh6.googleusercontent.com/-n7VYF50Z47M/VN0onnOtbdI/AAAAAAAAAYQ/lwyObtT3Xfo/w984-h209-no/153_chalkboard_bluegrey.jpg);
        width: 272px;
        height: 200px;
        background-repeat: no-repeat;
        background-size: 400px;

    }
</style>
<div class="container">
    <form class="form-inline">

        <%for (Course info : (ArrayList<Course>) request.getAttribute("courses")) {%>
        <div class="dodo">
            <h3 align="center" style="padding-top: 100px; "><%=info.getName()%>
            </h3>
            <%}%>
        </div>
    </form>
</div>
</html>
