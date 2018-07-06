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
    <script src="../JavaScript/panel.js?newversione"></script>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">

    <script src = "--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src = '../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel = "stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">

    <% User user = (User) request.getSession().getAttribute(Constraints.USER);
       String courseId = request.getParameter(Constraints.COURSE_ID);
    %>

    <script>

        let userProfilePicture = '<%=user.getPicturePath()%>';

        var content = ['<div class="btn-group-vertical">',
                       '<button type="button" class="btn btn-light">Add as seminar reader</button>',
                       '<button type="button" class="btn btn-light">Add as teacher assistant</button>',
                        '</div>'].join('');

        $(document).ready(function(){
            $('[data-toggle="popover"]').popover({
                html:true,
                placement : 'bottom',
                content: content,
                trigger : 'click'
            });
    });
</script>

</head>
<body>
    <div id="content">
        <table class="table" id="usersTable" style="width:100%">
            <tr>
                <th>Name</th>
                <th>Surname</th>
                <th>E-mail</th>
            </tr>
            <tr>
                <td>Giorgi var</td>
                <td>Bagdavadze</td>
                <td>gbagh16@freeuni.edu.ge</td>
                <td>
                    <div>
                        <a href="#" data-toggle="popover">
                            <span class="glyphicon glyphicon-option-vertical"></span>
                        </a>
                    </div>
                </td>
            </tr>
            <tr class="success">
                <td>Irakli</td>
                <td>Freeuni</td>
                <td>irakli.freeuni@freeuni.edu.ge</td>
                <td>
                    <div>
                        <a href="#" data-toggle="popover">
                            <span class="glyphicon glyphicon-option-vertical"></span>
                        </a>
                    </div>
                </td>
            </tr>
            <tr class="Danger">
                <td>Davit</td>
                <td>Bezhanishvili</td>
                <td>dbezh16@freeuni.edu.ge</td>
                <td>
                    <div>
                        <a href="#" data-toggle="popover">
                            <span class="glyphicon glyphicon-option-vertical"></span>
                        </a>
                    </div>
                </td>
            </tr>
        </table>
    </div>
</body>
</html>
