<%@ page import="static Data.Constraints.CLIENT_ID" %>
<html>

<head>
    <title>LINC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- Ajax --%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <%-- Bootstrap --%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
    <link href="Styles/assets/css/font-awesome.css" rel="stylesheet">
    <link href="Styles/assets/css/docs.css" rel="stylesheet">
    <link href="Styles/bootstrap-social.css" rel="stylesheet">

    <%-- My css --%>
    <link rel="stylesheet" href="Styles/style.css">


    <%-- Google's authorization --%>
    <script src="https://apis.google.com/js/platform.js?onload=load" async defer gapi_processed="true"></script>
    <meta name="google-signin-client_id"
          content=<%=CLIENT_ID%>>

</head>

<body>
<div class="container" style="text-align: center; padding-top: 30vh">
        <form id="signIn" action="GoogleLogin" method="post">
            <input type="hidden" name="idToken" value="">
        </form>
            <h2>Welcome! Connect With Google To Start using LINC  </h2>
            <br>
            <div style="display: inline-block" id="g-signin2"></div>
</div>
</body>

<script>

    function load() {
        gapi.signin2.render('g-signin2', {
            'scope': 'profile email https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly',
            'width': 240,
            'height': 50,
            'longtitle': true,
            'theme': 'dark',
            'onsuccess': onSucces,
            'onfailure': onFailure
        });
    }

    function onFailure() {

    }

    function onSucces(googleUser) {
        let id_token = googleUser.getAuthResponse().id_token;
        let form = document.getElementById("signIn");
        form.idToken.value = id_token;
        form.submit();
    }

</script>

</html>
