<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://apis.google.com/js/client:platform.js?onload=start" async defer></script>
<head>
    <script>


        function finalCallback(authResult)
        {
            let code = authResult['code'];
            if (code) {
                let form = document.forms[0];
                form.auth_code.value = code;
                form.submit();
            }

        }


        function start() {

            gapi.load('auth2', function () {
                let auth2 = gapi.auth2.init({
                    client_id: '108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com',
                    scope: "profile email https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly " +
                    "https://www.googleapis.com/auth/classroom.coursework.students.readonly https://www.googleapis.com/auth/classroom.coursework.students"
                });

                auth2.grantOfflineAccess().then(finalCallback);
            });

        }

    </script>
</head>


<body>
    <form action="${pageContext.request.contextPath}/GoogleLogin" method="post">
        <input type="hidden" name="auth_code" value="">
        <input type="hidden" name="action" value="register">
    </form>
</body>