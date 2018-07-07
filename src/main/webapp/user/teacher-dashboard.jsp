<%@ page import="Data.Constraints" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="com.google.api.services.classroom.model.Teacher" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.classroom.model.Student" %>
<%@ page import="Database.UserDAO" %>
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

        var contentForStudents = ['<div class="btn-group-vertical">',
                                  '<button type="button" class="btn btn-light">Add as seminar reader</button>',
                                  '<button type="button" class="btn btn-light">Add as teacher assistant</button>',
                                  '</div>'].join('');

        var content = ['<div class="btn-group-vertical">',
                        '<button type="button" class="btn btn-light">Remove</button>',
                        '</div>'].join('');

        $(document).ready(function(){
            $('[data-toggle="popover"]').popover({
                html:true,
                placement : 'bottom',
                content: content,
                trigger : 'click'
            });
            $('[data-toggle="studentPopover"]').popover({
                html:true,
                placement : 'bottom',
                content: contentForStudents,
                trigger : 'click'
            });
    });
</script>

</head>
<body>
    <div id="content">
        <div class="panel-group">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        <a data-toggle="collapse" href="#semReaderCol">Seminar Readers</a>
                    </h3>
                    <div id="semReaderCol" class="panel-collapse collapse">
                        <table class="table" id="semReadersTable" style="width:100%">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                            </tr>
                            <% List<User> semReaders = UserDAO.getSeminarReaders();
                                for(User semReader : semReaders){ %>
                            <tr>
                                <td><%=semReader.getFirstName()%></td>
                                <td><%=semReader.getLastName()%></td>
                                <td><%=semReader.getEmail()%></td>
                                <td>
                                    <div>
                                        <a href="#" data-toggle="popover">
                                            <span class="glyphicon glyphicon-option-vertical"></span>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                            <%}%>
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
                        <table class="table" id="teacherAssTable" style="width:100%">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                            </tr>
                            <% List<User> assistants = UserDAO.getTeacherAssistants();
                                for(User assistant : assistants){ %>
                            <tr>
                                <td><%=assistant.getFirstName()%></td>
                                <td><%=assistant.getLastName()%></td>
                                <td><%=assistant.getEmail()%></td>
                                <td>
                                    <div>
                                        <a href="#" data-toggle="popover">
                                            <span class="glyphicon glyphicon-option-vertical"></span>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                            <%}%>
                        </table>
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
                        <table class="table" id="studentsTable" style="width:100%">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                            </tr>
                            <% GAPIManager gapi = GAPIManager.getInstance();
                                List<Student> students = gapi.getStudents(user , courseId);
                                for(Student student : students){ %>
                            <tr>
                                <td><%=student.getProfile().getName().getGivenName()%></td>
                                <td><%=student.getProfile().getName().getFamilyName()%></td>
                                <td><%=student.getProfile().getEmailAddress()%></td>
                                <td>
                                    <div>
                                        <a href="#" data-toggle="studentPopover">
                                            <span class="glyphicon glyphicon-option-vertical"></span>
                                        </a>
                                    </div>
                                </td>
                            </tr>
                            <%}%>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
