<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="Data.Constraints" %>
<%@ page import="static Data.Constraints.USER" %>
<html>

<head>
    <% if (!Validate.isLogged(request.getSession())) {
            response.sendRedirect("../loginPage.jsp");
            return;
        };
    %>
    <title>LINC Dashboard</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../Styles/dashboard.css">
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
    <script src="../JavaScript/dashboard.js"></script>

    <%--Comment following line if you want to view as Student--%>
    <script src="../JavaScript/dashboard-instructor.js"></script>



    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">

    <script src = "--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel = "stylesheet" href="../codemirror-5.39.0/lib/codemirror.css">
    <script src = '../codemirror-5.39.0/lib/codemirror.js'></script>
    <script src = '../codemirror-5.39.0/mode/clike.js'></script>
    <script src = '../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel = "stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">

    <% User user = (User) request.getSession().getAttribute(USER);%>
    <% String courseId = request.getParameter(Constraints.ROOM_ID); %>

</head>

<body onload="onLoad()">
    <div class="fill">
        <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
        <%--<img src="../Images/panel-slide.png" id="panel-swipe">--%>
    </div>

    <div id="content">
        <div class="panel panel-default">
            <ul class="nav nav-tabs" id = "navbar">
                <li class="active"><a href="javascript:void(0)" onclick="navbarOnClick()">code_file1.cpp</a></li>
                <li><a href="javascript:void(0)" onclick="navbarOnClick()">code_file2.cpp</a></li>
                <li><a href="javascript:void(0)" onclick="navbarOnClick()">big_file.cpp</a></li>
                <li><a href="javascript:void(0)"  onclick="navbarOnClick()">not_found.cpp</a></li>
            </ul>


            <div class="panel-body">
                <div id = "code-panel">
                    <textarea readonly id = "code-content">

                    </textarea>
                </div>
                <div id = "notification-div">
                    <h1 id = "notification-text">Please select code interval!</h1>
                </div>
                <div id = "comment-panel-wrapper">
                    <div class = "media" id = "comment-panel" hidden>
                        <img class = "media-object media-left" id = "comment-profile-picture" src="../Images/temp_user_icon.svg">
                        <div class = "media-body" id = "comment-content">
                            <p class = "media-heading" id = "comment-user-name">Fname Lname</p>
                            <p class = "media-body" id = "comment-text">Suggestion Text</p>
                            <p id = "comment-date">Here goes Precise Date</p>
                        </div>
                    </div>
                    <div id = "comment-editor-wrapper" class = "editor-wrapper" hidden>
                        <form onsubmit="submitSuggestion(); return false;">
                            <textarea id = "comment-editor-content" class="editor-content" name="content"></textarea>
                            <br>
                            <button type="submit" class="btn btn-primary">Submit</button>
                            <button type="reset" class="btn btn-default" onclick="clearInterval()">Clear Suggestion</button>
                            <button type="button" class="btn btn-warning" onclick="toggleSuggestionType()">Warning</button>
                        </form>
                    </div>
                    <div id = "reply-editor-wrapper" class = "editor-wrapper" hidden>
                        <form onsubmit="submitComment(); return false;">
                            <textarea id = "reply-editor-content" class = "editor-content" name="content"></textarea>
                            <br>
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </form>
                    </div>
                </div>
            </div>

        </div>
    </div>
</body>

</html>
