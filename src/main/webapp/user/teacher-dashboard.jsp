<%@ page import="Data.Constraints" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="com.google.api.services.classroom.model.Teacher" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="../Styles/style.css">
    <% if (!Validate.isLogged(request.getSession())) {
        response.sendRedirect("../loginPage.jsp");
        return;
    };
    %>
    <title>Teacher Dashboard</title>
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
    <script src="../JavaScript/dashboard.js?newversione"></script>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">

    <script src = "--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src = '../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel = "stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">

    <% User user = (User) request.getSession().getAttribute(Constraints.USER);
       String courseId = request.getParameter(Constraints.COURSE_ID);
    %>

</head>
<body>
  <div class="fill">
      <img src=<%=user.getPicturePath()%> class="img-circle" alt="Cinque Terre" id="user-panel-img">
  </div>
</body>
</html>
