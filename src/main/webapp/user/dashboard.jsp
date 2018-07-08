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
<html>

<head>
    <title>LINC Dashboard</title>
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
    <script src="../JavaScript/dashboard.js?newversione"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>

<%--Comment following line if you want to view as Student--%>
    <script src="../JavaScript/dashboard-instructor.js?newversion"></script>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">
    <link rel="stylesheet" href="../Styles/dashboard.css">

    <script src="--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../codemirror-5.39.0/lib/codemirror.css">
    <script src='../codemirror-5.39.0/lib/codemirror.js'></script>
    <script src='../codemirror-5.39.0/mode/clike.js'></script>
    <script src='../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel="stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">
    <script src="../JavaScript/panel.js"></script>

    <% User user = (User) request.getSession().getAttribute(USER);%>
    <% String courseId = request.getParameter(Constraints.COURSE_ID); %>
    <% AssignmentInfoDAO assignmentInfoDAO = (AssignmentInfoDAO) request.getServletContext().getAttribute(ASSIGNMENT_INFO_DAO); %>
    <% GAPIManager gapiManager = GAPIManager.getInstance(); %>

    <%
        Set<String> assignedAssIds = new HashSet<>(assignmentInfoDAO.getAssignmentIds(courseId));
        assignedAssIds.forEach(System.out::println);
        List<Assignment> assignments = gapiManager.getUserAssignments(user.getAccessToken(), user.getRefreshToken(), courseId).stream()                // convert list to stream
                .filter(assignment -> true).collect(Collectors.toList());
        assignments.forEach(System.out::println);
    %>

    <script>
        let userProfilePicture = '<%=user.getPicturePath()%>';
    </script>

</head>

<body onload="onLoad()">
<div class="fill">
    <div style="cursor:pointer" onclick="togleNav()">
        <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img"></div>
    <div id="menuBar" onclick="togleNav()"><span class="glyphicon">&#xe236;</span>
    </div>
</div>
<div id="mySidenav" class="sidenav">
    <div class="sidenav-container" style="margin-top: 10px">
        <div class="sidenav-item">
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

<div id="content">
    <div id = "loader-wrapper">
        <div class="loader"></div>
    </div>
    <div class="panel panel-default">
        <ul class="nav nav-tabs" id="navbar">
            <li id="navbar-element" hidden><a href="javascript:void(0)" onclick="navbarOnClick()"></a></li>
        </ul>

        <div class="panel-body">
            <div id="code-panel">
                    <textarea readonly id="code-content">

                    </textarea>
            </div>
            <div id="notification-div">
                <h1 id="notification-text">Please select code interval!</h1>
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
                </div>
                <div id="comment-editor-wrapper" class="editor-wrapper" hidden>
                    <form>
                        <textarea id="comment-editor-content" class="editor-content" name="content"></textarea>
                        <br>
                        <button type="button" class="btn btn-primary" onclick="submitSuggestion()">Submit</button>
                        <button type="reset" class="btn btn-default" onclick="clearInterval();">Clear Suggestion
                        </button>
                        <button id="suggestion-type" type="button" class="btn btn-warning"
                                onclick="toggleSuggestionType()">Warning
                        </button>
                    </form>
                </div>
                <div id="reply-editor-wrapper" class="editor-wrapper" hidden>
                    <form>
                        <textarea id="reply-editor-content" class="editor-content" name="content"></textarea>
                        <br>
                        <button type="button" class="btn btn-primary" onclick="submitReply()">Submit</button>
                    </form>
                </div>
            </div>
        </div>

    </div>
</div>
</body>

</html>
