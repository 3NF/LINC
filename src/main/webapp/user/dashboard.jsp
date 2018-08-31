<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="Data.Constraints" %>
<%@ page import="static Data.Constraints.USER" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="Database.AssignmentInfoDAO" %>
<%@ page import="static Data.Constraints.ASSIGNMENT_INFO_DAO" %>
<%@ page import="static Data.Constraints.GAPI_MANAGER" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="Models.Assignment" %>
<%@ page import="static Data.Constraints.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="org.apache.http.HttpStatus" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.concurrent.*" %>
<html>

<head>
    <title>LINC Dashboard</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="client_id" content="<%=CLIENT_ID%>">

    <%--Bootstrap and Jquery --%>
    <script
            src="https://code.jquery.com/jquery-3.3.1.min.js"
            integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="../Styles/bootstrap-social.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <%--Google API--%>
    <script src="https://apis.google.com/js/platform.js" async defer></script>

    <%--Local Scripts--%>
    <script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/dashboard.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/panel.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/project-view.js"></script>
    <script src="${pageContext.request.contextPath}/jstree/src/jstree.js"></script>
    <script src="${pageContext.request.contextPath}/JavaScript/html-templates.js"></script>

    <%--Local Styles--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/dashboard.css">

    <%--CodeMirror--%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/codemirror.min.css">
    <script src='https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/codemirror.js'></script>
    <script src='https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.39.2/mode/clike/clike.js'></script>

    <%--BootstrapMarkdown--%>
    <script src='${pageContext.request.contextPath}/bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-markdown/css/bootstrap-markdown.min.css">

    <%--JSTree--%>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/jstree.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/jstree/src/themes/default/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jstree/3.2.1/themes/default/style.min.css">

    <% User user = (User) request.getSession().getAttribute(USER);%>
    <% String courseId = request.getParameter(Constraints.COURSE_ID); %>
    <% AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO); %>
    <% GAPIManager gapiManager = GAPIManager.getInstance(); %>
    <% ExecutorService executor = Executors.newFixedThreadPool(10); %>
    <% String courseID = request.getParameter(COURSE_ID); %>

    <%
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println("დაიწყო  დეშბორდი  " + dateFormat.format(date));
        Future<String> teacherIdFuture = executor.submit(() -> UserDAO.getUserIDsByRole(courseID, UserDAO.Role.Teacher).get(0));
        Set<String> assignedAssIds = new HashSet<>(assignmentInfoDAO.getAssignmentIds(courseId));
        List<Assignment> assignments = gapiManager.getCourseAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream()                // convert list to stream
                .filter(assignment -> assignedAssIds.contains(assignment.getId())).collect(Collectors.toList());

        if (assignments.size() == 0) {
            HelperClasses.Utilities.sendError(request, response, HttpStatus.SC_NOT_FOUND, "Lecturer hasn't released any assignment to system!");
            return;
        }

        String firstAssignmentID = assignments.get(0).getId();
        if (request.getParameter(ASSIGNMENT_ID) == null) {
            response.sendRedirect("dashboard.jsp?" + COURSE_ID + "=" + request.getParameter(COURSE_ID) + "&" + ASSIGNMENT_ID + "=" + firstAssignmentID);
            return;
        }

        date = new Date();
        System.out.println("დავალებები წამოიღო    " + dateFormat.format(date));
    %>

    <%
        boolean isStudent = request.getParameter(USER_ID) == null;
        List<String> assignmentHtmlIds = new ArrayList<>();
        Map<String, String> hmp;
        if (isStudent) {
            hmp = new HashMap<>();
            List<Pair<String, String>> grades = assignmentInfoDAO.getUsersGrades(request.getParameter(COURSE_ID), user.getUserId());
            for (Pair<String, String> grade : grades) {
                hmp.put(grade.getKey(), grade.getValue());
            }
        } else {
            hmp = Collections.emptyMap();
        }

        for (Assignment assignment : assignments) {
            String grade = hmp.get(assignment.getId());
            assignmentHtmlIds.add("grade_" + (grade == null ? "none" : grade.replaceAll("\\s+", "")));
        }

        date = new Date();
        System.out.println("ნიშნები წამოიღო    " + dateFormat.format(date));

        String teacherId = null;
        try {
            teacherId = teacherIdFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        date = new Date();
        System.out.println("მასწავლებელი წამოიღო    " + dateFormat.format(date));


        if (!isStudent) {%>
    <%--Comment following line if you want to view as Student--%>
    <script src="${pageContext.request.contextPath}/JavaScript/dashboard-instructor-controls.js"></script>
    <%}%>


    <script>let assignmentID = <%=assignments.get(0).getId()%>;</script>
    <script>let uid = <%=request.getParameter(USER_ID)%>;</script>
    <script>let teachID = '<%=teacherId%>';</script>

    <%
        String assignmentID = request.getParameter(ASSIGNMENT_ID);
        String userID = user.getUserId();
    %>

</head>

<body onload="onLoad()">
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
        <div class="sidenav-container" style="height: 20%">
            <% for (int i = 0; i < assignments.size(); i++) {
                Assignment assignment = assignments.get(i);
                String idInHtml = assignmentHtmlIds.get(i);
                System.out.println(idInHtml);
            %>
            <div class="sidenav-item <%=idInHtml%>" onclick=getAssignment(<%=assignment.getId()%>)>
                <% String grade = idInHtml.substring(6); %>
                <p onmouseover="mouseOver('<%=grade%>')"
                   onmouseout="this.innerHTML='<%=assignment.getName()%>';"
                ><%=assignment.getName()%>
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
    <div id="jstree_demo_div_container">
        <div id="jstree_demo_div">
        </div>
    </div>
    <div id="toggle-project-view" onclick="toggleProjectView()" data-toggle="tooltip" title="Toggle project view"><span
            id="file-open" class="glyphicon">&#xe117;</span></div>
    <div id="loader-wrapper">
        <div class="loader"></div>
    </div>
    <div class="panel panel-default">

        <div class="panel-body">
            <div id="code-panel">
                <label for="code-content"></label><textarea readonly id="code-content">

                        </textarea>
            </div>
            <div id="comment-panel-wrapper">
                <div class="media" id="comment-panel" hidden>
                    <img class="media-object media-left" id="comment-profile-picture"
                         src="../Images/temp_user_icon.svg">
                    <div class="media-body" id="comment-content">
                        <p class="media-heading" id="comment-user-name">Fname Lname</p>
                        <p class="media-body" id="comment-text">Suggestion Text</p>
                        <p id="comment-date">Here goes Precise Date</p>
                    </div>
                    <%
                        if (request.getParameter(USER_ID) != null) {
                    %>
                    <button id="suggestion-remove-btn" type="button" class="btn btn-danger" onclick="deleteClick()"
                            hidden>Remove
                    </button>
                    <%
                        }
                    %>
                </div>
                <div id="comment-editor-wrapper" class="editor-wrapper" hidden>
                    <form onsubmit="submitSuggestion(); return false;">
                        <textarea id="comment-editor-content" class="editor-content" name="content"
                                  title="def"></textarea>
                        <br>
                        <button type="submit" class="btn btn-primary">Submit</button>
                        <button type="reset" class="btn btn-default" onclick="clearInterval();">Clear Suggestion
                        </button>
                        <button id="suggestion-type" type="button" class="btn btn-warning" onclick="toggleSuggestionType()">Warning</button>
                    </form>
                </div>

                <div id="reply-editor-wrapper" class="editor-wrapper" hidden>
                    <form onsubmit="submitReply(); return false;">
                        <textarea placeholder="Your reply goes here!" id="reply-editor-content" class="editor-content"
                                  name="content" title="def"></textarea>
                        <br>
                        <button type="submit" class="btn btn-primary">Submit</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div id="instructorButton">
        <select id='gradeSelector' class="form-control selectpicker show-tick" data-style="btn-primary" name="grade"
                onchange="updateGrade(this)" title="def">
            <option></option>
            <option>Plus Plus</option>
            <option>Plus</option>
            <option>Check Plus</option>
            <option>Check</option>
            <option>Check Minus</option>
            <option>Minus</option>
            <option>Minus Minus</option>
            <option>0</option>
        </select>
    </div>
</div>
</body>

</html>
