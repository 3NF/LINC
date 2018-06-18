<%@ page import="static Data.Constraints.CLIENT_ID" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>

<head>
    <title>LINC</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="google-signin-client_id"
          content=<%=CLIENT_ID%>>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/Styles/style.css">
    <link href="${pageContext.request.contextPath}/Styles/assets/css/bootstrap.css" rel="stylesheet">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
    <script>


        function start()
        {
            gapi.load('auth2', function () {
                auth2 = gapi.auth2.init({
                    client_id: '108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com',
                    scope: "profile email https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly"
                });
            });

        }

        function finalCallback(authResult)
        {
            let code = authResult['code'];
            if (code) {
                let form = document.forms[0];
                form.auth_code.value = code;
                form.submit();
            }


        }

        function onSignIn()
        {
            auth2.grantOfflineAccess().then(finalCallback);
        }

    </script>

</head>

<body>
<div class="container" style="text-align: center; padding-top: 30vh">
    <form action="${pageContext.request.contextPath}/GoogleLogin" method="post">
        <input type="hidden" name="auth_code" value="">
    </form>
    <h2>Welcome! Connect With Google To Start using LINC </h2>
    <br>
    <button id="signin" onclick="onSignIn()"></button>
</div>
</body>

</html>
