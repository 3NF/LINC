<%@ page import="Models.User" %>
<%@ page import="Models.BasicRoomInfo" %>
<%@ page import="javax.validation.Valid" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        if (!Validate.isSesion(session)) {response.sendRedirect("../loginPage.jsp"); return;}
        //for testing
        User user = new User("g.chxikvadze.14.12@gmail.com", "Giorgi", "Chkhikvadze","g.chxikvadze.14.12@gmail.com","er","er");
        BasicRoomInfo[] roomInfos = {
                new BasicRoomInfo("ChudoSchool", "ChudoSchool", "Giorgi Chkhivadze","er"),
                new BasicRoomInfo("BezhoyStudy", "BezhoyStudy", "Davit Bezhanishvili","er")
        };
    %>
</head>
<body class="text-center">
    <iframe id="gClassroomFrame" style="display: none" src="https://classroom.google.com/u/0/h"></iframe>
    <h3>Welcome <%=user.getFirstName()%>! Choose ClassRoom To Enter:</h3>
    <ul>
        <%for (BasicRoomInfo info : roomInfos)
        {%>
        <li>
            <a href="rooms?name=<%=info.getId()%>"><%=info.getTitle()%></a>
        </li>
        <%}%>
    </ul>
</body>
</html>
