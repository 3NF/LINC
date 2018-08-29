<%@ page import="Data.Constraints" %>
<%@ page import="Models.User" %>
<%@ page import="Models.Assignment" %>
<%@ page import="static Data.Constraints.ASSIGNMENT_INFO_DAO" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="static Data.Constraints.SECTION_DAO" %>
<%@ page import="static Data.Constraints.COURSE_ID" %>
<%@ page import="static Data.Constraints.CLIENT_ID" %>
<%@ page import="Database.*" %>
<%@ page import="static Data.Constraints.*" %>
<%@ page import="com.google.api.services.classroom.model.Student" %>
<%@ page import="org.apache.http.HttpStatus" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.concurrent.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="client_id" content="<%=CLIENT_ID%>">

    <%--bootstrap--%>
    <script
            src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>

    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/instructor-dashboard.js?newversione"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/panel.js"></script>

    <%--Comment following line if you want to view as Student--%>
    <script src="${pageContext.request.contextPath}/JavaScript/dashboard-instructor-controls.js?newversion"></script>

    <%--my css--%>


    <script src="--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src='${pageContext.request.contextPath}/bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <script src="${pageContext.request.contextPath}/JavaScript/gapi-scripts.js"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
    <script src="https://apis.google.com/js/api.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="${pageContext.request.contextPath}/Styles/bootstrap-social.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-markdown/css/bootstrap-markdown.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/instructor-dashboard.css">


    <% AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO); %>
    <%

        SectionDAO DAO = (SectionDAO) request.getServletContext().getAttribute(SECTION_DAO);
        UserStorage userStorage = (UserStorage) request.getServletContext().getAttribute(USER_STORAGE);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println("დაიწყო    " + dateFormat.format(date));
        User user = (User) request.getSession().getAttribute(Constraints.USER);
        String courseId = request.getParameter(Constraints.COURSE_ID);
        UserDAO.Role userRole = UserDAO.getRoleByCourse(user, courseId);

        if (userRole != UserDAO.Role.TeacherAssistant && userRole != UserDAO.Role.SeminarReader) {
            response.sendRedirect("choose-room.jsp");
            return;
        }
        date = new Date();
        System.out.println("შეამოწმა    " + dateFormat.format(date));


        CompletableFuture<List<Assignment>> assignmentsFuture = CompletableFuture.supplyAsync(() ->
                new HashSet<>(assignmentInfoDAO.getAssignmentIds(courseId)), executor)
                .thenApply(assignedAssIds -> GAPIManager.getInstance().getCourseAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream()                // convert list to stream
                        .filter(assignment -> assignedAssIds.contains(assignment.getId())).collect(Collectors.toList()))
                .toCompletableFuture();

        Future<String> teacherIdFuture = executor.submit(() -> UserDAO.getUserIDsByRole(courseId, UserDAO.Role.Teacher).get(0));

        CompletableFuture<List<User>> studentsFuture = CompletableFuture.supplyAsync(() -> DAO.getUsersInSection(courseId, user.getUserId()), executor).thenApply(strings -> {
            try {
                return userStorage.getUsersWithIds(teacherIdFuture.get(), strings);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).toCompletableFuture();

    %>
    <%
        List<Assignment> assignments;
        try {
            assignments = assignmentsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            request.getRequestDispatcher("/error-page").forward(request, response);
            return;
        }

        if (assignments.size() == 0) {
            HelperClasses.Utilities.sendError(request, response, HttpStatus.SC_NOT_FOUND, "Lecturer hasn't released any assignment to system!");
            return;
        }
        date = new Date();
        System.out.println("დავალებები წამოიღო    " + dateFormat.format(date));
    %>

    <script>let assignmentID = <%=assignments.get(0).getId()%>;</script>
    <script>courseID = '<%=courseId%>';</script>
    <title>Section View</title>

</head>
<body>
<div class="fill">
    <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
    <button id="menuBar" onclick="toggleNav()"><span></span><span></span><span></span>
    </button>
</div>

<div id="content">
    <div id="mySidenav" class="sidenav">
        <div class="sidenav-container" style="margin-top: 10px">
            <div class="sidenav-item" id="goHome">
                <p><span class="glyphicon glyphicon-home"></span> Classes</p>
            </div>
        </div>
        <div class="sprt" aria-disabled="true" role="separator" style="user-select: none;"></div>
        <div class="sidenav-container" style="height: 90%">
            <% for (Assignment assignment : assignments) {%>
            <div class="sidenav-item" onclick=getAssignment(<%=assignment.getId()%>)>
                <p><%=assignment.getName()%>
                </p>
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
    <%
        List<User> students = Collections.emptyList();
        try {
            students = studentsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        date = new Date();
        System.out.println("სტუდენტები წამოიღო სურათებიანად    " + dateFormat.format(date));
    %>
    <% for (User student : students) {%>
    <div class="user-box" align="center" onclick="chooseStudent('<%=student.getUserId()%>')">
        <img class="user-img" src="<%=student.getPicturePath()%>">
        <h3 class="user-name"><%=student.getFirstName() + " " + student.getLastName()%>
        </h3>
        <div class = "user-point grade_Plus">Not Graded</div>
    </div>
    <%}%>
</div>
</body>
</html>
