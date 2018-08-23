<%@ page import="Data.Constraints" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="com.google.api.services.classroom.model.Teacher" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.classroom.model.Student" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="com.google.api.services.classroom.model.UserProfile" %>
<%@ page import="Database.AssignmentInfoDAO" %>
<%@ page import="static Data.Constraints.ASSIGNMENT_INFO_DAO" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%@ page import="Models.Assignment" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="static Data.Constraints.CLIENT_ID" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="static Data.Constraints.USER_ID" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="../Styles/style.css">
    <title>Teacher Dashboard</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="client_id" content="<%=CLIENT_ID%>">
    <link rel="stylesheet" href="../Styles/dashboard.css">
    <%--bootstrap--%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/Styles/bootstrap-social.css" rel="stylesheet">
    <script
            src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>

    <%--my css--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/teacher-dashboard.css">

    <script src = "--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src = '${pageContext.request.contextPath}/bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel = "stylesheet" href="${pageContext.request.contextPath}/bootstrap-markdown/css/bootstrap-markdown.min.css">


    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
    <script src="https://apis.google.com/js/api.js"></script>


    <script src="${pageContext.request.contextPath}/JavaScript/teacher-dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/panel.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/gapi-scripts.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/html-templates.js"></script>


    <%
        User user = (User) request.getSession().getAttribute(Constraints.USER);
        String courseId = request.getParameter(Constraints.COURSE_ID);
    %>
    <% AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO); %>
    <% GAPIManager gapiManager = GAPIManager.getInstance(); %>


    <%
        Set<String> assignedAssIds = new HashSet<>(assignmentInfoDAO.getAssignmentIds(courseId));
        List<Assignment> uploaded = gapiManager.getCourseAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream().filter
                (assignment -> assignedAssIds.contains(assignment.getId())).collect(Collectors.toList());
        List <Assignment> notUploaded = gapiManager.getCourseAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream().filter
                (assignment -> !assignedAssIds.contains(assignment.getId())).collect(Collectors.toList());
    %>

    <%
        List<String> semReaderIds = UserDAO.getUserIDsByRole(courseId, UserDAO.Role.SeminarReader);
        List<String> teacherAssIds = UserDAO.getUserIDsByRole(courseId, UserDAO.Role.TeacherAssistant);
        Set<String> studentsIds = new HashSet<>();
        String teacherID = ((User) session.getAttribute(Constraints.USER)).getUserId();
        UserDAO.addUser(teacherID,UserDAO.Role.Teacher,courseId);
        String teacherAssistantJson = new Gson().toJson(teacherAssIds);
        String semReadersJson = new Gson().toJson(semReaderIds);
    %>
    <script>userId = '<%=user.getUserId()%>';</script>
    <script>courseID = '<%=courseId%>';</script>
    <script>students = []</script>
    <script>seminarReaders = []</script>
    <script>assistants = []</script>
    <script>classroomId = getParameter("courseID"); </script>
    <script>assistantIds = JSON.parse('<%=teacherAssistantJson%>') </script>
    <script>seminarReaderIds = JSON.parse('<%=semReadersJson%>') </script>
</head>
<body>
<div class="fill">
    <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
    <button id="menuBar" onclick="toggleNav()"><span></span><span></span><span></span>
    </button>
</div>
<div id="mySidenav" class="sidenav">
    <div class="sidenav-container" style="margin-top: 10px">
        <div class="sidenav-item" id = "goHome">
            <p><span class="glyphicon glyphicon-home"></span>     Classes</p>
        </div>
    </div>
    <div class="sprt" aria-disabled="true" role="separator" style="user-select: none;"></div>
    <div class="sidenav-container" style="height: 90%">
        <% for (Assignment assignment : uploaded) {%>
        <div class="sidenav-item"  onclick=isDownloaded('<%=assignment.getId()%>')>
            <p style="color: green"><%=assignment.getName()%></p>
        </div>
        <%}%>

        <% for (Assignment assignment : notUploaded) {%>
        <div class="sidenav-item"  onclick=sendAssignments('<%=assignment.getId()%>')>
            <p style="color: red"><%=assignment.getName()%></p>
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
<div id="content">
    <div class="panel-group">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <a data-toggle="collapse" href="#semReaderCol">Seminar Readers</a>
                </h3>
                <div id="semReaderCol" class="panel-collapse collapse">
                    <table class="table" id="semReadersTable">
                        <tr>
                            <th>Name</th>
                            <th>Surname</th>
                            <th>E-mail</th>
                            <th></th>
                        </tr>
                        <tbody id = "semReadersTable1">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-group">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <a data-toggle="collapse" href="#teacherAssCol">Teacher Assistants</a>
                </h3>
                <div id="teacherAssCol" class="panel-collapse collapse">
                    <table class="table" id="teacherAssTable">
                        <tr>
                            <th>Name</th>
                            <th>Surname</th>
                            <th>E-mail</th>
                        </tr>
                        <tbody id = "teacherAssTable1">
                        </tbody>
                    </table>
                    <button class = "btn btn-light" style="width:260px;color:black" onclick='randomSections()'> Randomise sections! </button>
                </div>
            </div>
        </div>
    </div>
    <div class="panel-group">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <a data-toggle="collapse" href="#studentsCol">Students</a>
                </h3>
                <div id="studentsCol" class="panel-collapse collapse">
                    <table class="table" id="studentsTable">
                        <tr>
                            <th>Name</th>
                            <th>Surname</th>
                            <th>E-mail</th>
                        </tr>
                        <tbody id = "studentsTable1">
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="hide">
    <div class="btn-group-vertical" id="addEx">
        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
            <span class="glyphicon glyphicon-option-vertical"></span></button>
        <ul class="dropdown-menu" >
            <li><button type="button" class="btn btn-light" onclick="changeRole()" id="addExButtonSR">
                Add as seminar reader
            </button>
            </li>
            <li><button type="button" class="btn btn-light" onclick="changeRole()" id="addExButtonTA">
                Add as teacher assistant
            </button>
            </li>
        </ul>
    </div>
    <div class="btn-group-vertical" id="removeEx">
        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
            <span class="glyphicon glyphicon-option-vertical"></span></button>
        <ul class="dropdown-menu">
            <li><button type="button" class="btn btn-light" onclick="changeRole()" id="removeExButton">Remove</button>
            </li>
        </ul>
    </div>
</div>
</body>
<script>
    gapi.load('client', get_students);
</script>
</html>
