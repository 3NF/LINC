<%@ page import="Data.Constraints" %>
<%@ page import="Database.GAPIManager" %>
<%@ page import="HelperClasses.Validate" %>
<%@ page import="Models.User" %>
<%@ page import="com.google.api.services.classroom.model.Teacher" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.api.services.classroom.model.Student" %>
<%@ page import="Database.UserDAO" %>
<%@ page import="com.google.api.services.classroom.model.UserProfile" %>
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
    <script src="../JavaScript/teacher-dashboard.js?newversione"></script>

    <%--my css--%>
    <link rel="stylesheet" href="../Styles/style.css">
    <link rel="stylesheet" href="../Styles/teacher-dashboard.css">

    <script src = "--https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src = '../bootstrap-markdown/js/bootstrap-markdown.js'></script>
    <link rel = "stylesheet" href="../bootstrap-markdown/css/bootstrap-markdown.min.css">

    <% User user = (User) request.getSession().getAttribute(Constraints.USER);
       String courseId = request.getParameter(Constraints.COURSE_ID);
    %>

    <script>

        let userProfilePicture = '<%=user.getPicturePath()%>';

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
                        <table class="table" id="semReadersTable">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                                <th></th>
                            </tr>
                            <% List<UserProfile> semReaders = UserDAO.getSeminarReaders(user , courseId);
                                for(UserProfile semReader : semReaders){ %>
                            <tr>
                                <td><%=semReader.getName().getGivenName()%></td>
                                <td><%=semReader.getName().getFamilyName()%></td>
                                <td><%=semReader.getEmailAddress()%></td>
                                <td>
                                    <div class="btn-group-vertical">
                                        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
                                            <span class="glyphicon glyphicon-option-vertical"></span></button>
                                        <ul class="dropdown-menu">
                                            <li><button type="button" class="btn btn-light" onclick="changeRole('<%=UserDAO.Role.SeminarReader%>' , <%=semReader.getId()%> , 'remove')">Remove</button></li>
                                        </ul>
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
                        <table class="table" id="teacherAssTable">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                            </tr>
                            <% List<UserProfile> assistants = UserDAO.getTeacherAssistants(user , courseId);
                                for(UserProfile assistant : assistants){ %>
                            <tr>
                                <td><%=assistant.getName().getGivenName()%></td>
                                <td><%=assistant.getName().getFamilyName()%></td>
                                <td><%=assistant.getEmailAddress()%></td>
                                <td>
                                    <div class="btn-group-vertical">
                                        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
                                            <span class="glyphicon glyphicon-option-vertical"></span></button>
                                        <ul class="dropdown-menu">
                                            <li><button type="button" class="btn btn-light" onclick="changeRole('<%=UserDAO.Role.TeacherAssistant%>' , <%=assistant.getId()%> , 'remove')">Remove</button></li>
                                        </ul>
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
                        <table class="table" id="studentsTable">
                            <tr>
                                <th>Name</th>
                                <th>Surname</th>
                                <th>E-mail</th>
                            </tr>
                            <% List<UserProfile> students = UserDAO.getStudents(user , courseId);
                                for(UserProfile student : students){ %>
                            <tr>
                                <td><%=student.getName().getGivenName()%></td>
                                <td><%=student.getName().getFamilyName()%></td>
                                <td><%=student.getEmailAddress()%></td>
                                <td>
                                    <div class="btn-group-vertical">
                                        <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown">
                                            <span class="glyphicon glyphicon-option-vertical"></span></button>
                                        <ul class="dropdown-menu">
                                            <li><button type="button" class="btn btn-light" onclick="changeRole('<%=UserDAO.Role.SeminarReader%>' , <%=student.getId()%> , 'add')">Add as seminar reader</button></li>
                                            <li><button type="button" class="btn btn-light" onclick="changeRole('<%=UserDAO.Role.TeacherAssistant%>' , <%=student.getId()%> , 'add')">Add as teacher assistant</button></li>
                                        </ul>
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
