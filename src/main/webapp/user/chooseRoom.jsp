<%@ page import="Models.User" %>
<%@ page import="Models.Room" %>
<%@ page import="Models.BasicRoomInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="../Styles/chooseRoom.css" rel="stylesheet">
    <title>Choose Room - LINC</title>
    <%
        //for testing
        User user = new User("g.chxikvadze.14.12@gmail.com", "Giorgi", "Chkhikvadze");
        BasicRoomInfo[] roomInfos = {
                new BasicRoomInfo("ChudoSchool", "ChudoSchool", "Giorgi Chkhivadze"),
                new BasicRoomInfo("BezhoyStudy", "BezhoyStudy", "Davit Bezhanishvili")
        };
    %>
</head>
<body class="text-center">
    <h3>Welcome <%=user.getFirstName()%>! Choose Room To Enter:</>
    <ul>
        <%for (BasicRoomInfo info : roomInfos)
        {%>
        <li>
            <a class="title" href="rooms?name=<%=info.getId()%>"><%=info.getTitle()%></a><br>
        </li><br>
        <%}%>
    </ul>
</body>
</html>
