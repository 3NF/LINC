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

        function onSuccess(googleUser)
        {
            $(".g-signin2").hide();
            $("#loading").show();
            let id_token = googleUser.getAuthResponse().id_token;
            let form = document.forms[0];
            form.id_token.value = id_token;
            form.submit();
        }

    </script>

</head>

<body>
<div class="container" style="text-align: center; padding-top: 30vh">
    <form action="${pageContext.request.contextPath}/GoogleLogin" method="post">
        <input type="hidden" name="id_token" value="">
        <input type="hidden" name="action" value="login">
    </form>
    <h2>Welcome! Connect With Google To Start using LINC </h2>
    <br>
    <div style="text-align: center; margin: 0 auto; width: fit-content">
        <img id="loading" src="Images/loading.gif" style="display: none">
        <div class="g-signin2" data-onsuccess="onSuccess"></div>
    </div>
</div>
</body>

</html>
