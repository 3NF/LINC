<%@ page import="Data.Constraints" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="Models.User" %>
<%@ page import="Models.Assignment" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="Database.AssignmentInfoDAO" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="static Data.Constraints.ASSIGNMENT_INFO_DAO" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%--bootstrap--%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="../Styles/bootstrap-social.css" rel="stylesheet">
    <script
            src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <script src="../JavaScript/instructor-dashboard.js?newversione"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
    <script src="../JavaScript/panel.js"></script>

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


    <% AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO); %>
    <% GAPIManager gapiManager = GAPIManager.getInstance(); %>

    <%

        User user = (User) request.getSession().getAttribute(Constraints.USER);
        String courseId = request.getParameter(Constraints.COURSE_ID);

        UserDAO.Role userRole = UserDAO.getRoleByCourse(user, courseId);
        System.out.println(userRole);
        if (userRole != UserDAO.Role.TeacherAssistant && userRole != UserDAO.Role.SeminarReader) {
            response.sendRedirect("choose-room.jsp");
            return;
        }

        Set<String> assignedAssIds = new HashSet<>(assignmentInfoDAO.getAssignmentIds(courseId));
        List<Assignment> assignments = gapiManager.getCourseAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream()                // convert list to stream
                .filter(assignment -> assignedAssIds.contains(assignment.getId())).collect(Collectors.toList());

    %>

    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>

    <title>Title</title>

</head>
<body>
<div class="fill">
    <div style="cursor:pointer" onclick="togleNav()">
        <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img"></div>
    <div id="menuBar" onclick="togleNav()"><span class="glyphicon">&#xe236;</span>
    </div>
</div>
<div id="mySidenav" class="sidenav">
    <div class="sidenav-container" style="margin-top: 10px">
        <div class="sidenav-item" id = "goHome">
            <p><span class="glyphicon glyphicon-home"></span>     Classes</p>
        </div>
    </div>
    <div class="sprt" aria-disabled="true" role="separator" style="user-select: none;"></div>
    <div class="sidenav-container" style="height: 90%">
        <% for (Assignment assignment : assignments) {%>
        <div class="sidenav-item" onclick=getAssignment(<%=assignment.getId()%>)>
            <p><%=assignment.getName()%></p>
        </div>
        <%}%>
    </div>
    <div class="sprt" aria-disabled="true" role="separator" style="user-select: none;"></div>
    <div class="sidenav-container" style="margin-top: 10px">
        <div class="sidenav-item">
            <p onclick="signOut()">Logout</p>
        </div>
    </div>
</div>
</body>
</html>
