<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="HelperClasses.Validate" %>
<html>

<head>
    <% if (!Validate.isLogged(request.getSession())) {
            response.sendRedirect("../loginPage.jsp");
            return;
        };
    %>
    <title>LINC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%--bootstrap--%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="../Styles/assets/css/bootstrap.css" rel="stylesheet">
    <link href="../Styles/assets/css/font-awesome.css" rel="stylesheet">
    <link href="../Styles/assets/css/docs.css" rel="stylesheet">
    <link href="../Styles/bootstrap-social.css" rel="stylesheet">

    <%@ page import="Models.User" %>
    <%@ page import="HelperClasses.Validate" %>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">

    <script src="--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>


    <% User user = (User) request.getSession().getAttribute("user");%>

</head>

<body>
</body>

</html>
