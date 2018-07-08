<%@ page import="Data.Constraints" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="Models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%--bootstrap--%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="../Styles/assets/css/bootstrap.css" rel="stylesheet">
    <link href="../Styles/assets/css/font-awesome.css" rel="stylesheet">
    <link href="../Styles/assets/css/docs.css" rel="stylesheet">
    <link href="../Styles/bootstrap-social.css" rel="stylesheet">
    <script
            src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <script src="../JavaScript/instructor-dashboard.js?newversione"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>

    <%--Comment following line if you want to view as Student--%>
    <script src="../JavaScript/dashboard-instructor-controls.js?newversion"></script>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">
    <link rel="stylesheet" href="../Styles/instructor-dashboard.css">

    <script src="--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../codemirror-5.39.0/lib/codemirror.css">
    <script src='../codemirror-5.39.0/lib/codemirror.js'></script>
    <script src='../codemirror-5.39.0/mode/clike.js'></script>
    <script src='../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel="stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">
    <script src="../JavaScript/panel.js"></script>

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
