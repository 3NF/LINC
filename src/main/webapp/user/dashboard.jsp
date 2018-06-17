<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="HelperClasses.Validate" %>
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
    <script src="../JavaScript/dashboard.js"></script>

    <%@ page import="Models.User" %>
    <%@ page import="HelperClasses.Validate" %>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">

    <script src="--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <% User user = (User) request.getSession().getAttribute("user");%>

</head>

<body onload="onLoad()">
    <div class="fill">
        <img src="../Images/temp_user_icon.svg" id="user-panel-img">
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
                <div id = "code-content"> Code goes here!</div>
            </div>
        </div>
    </div>
</body>

</html>
