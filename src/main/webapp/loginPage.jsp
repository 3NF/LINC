<%@ page import="static Data.Constraints.CLIENT_ID" %>
<html>

<head>
    <title>LINC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- My css --%>
    <link rel="stylesheet" href="Styles/style.css">


    <%-- Google's authorization --%>
    <script src="https://apis.google.com/js/platform.js" async defer></script>

</head>

<body onload="load()">
<div class="container" style="text-align: center; padding-top: 30vh">
        <form id="signIn" action="GoogleLogin" method="post">
            <input type="hidden" name="idToken" value="">
        </form>
            <h2>Welcome! Connect With Google To Start using LINC  </h2>
            <br>
            <div style="display: inline-block; width: 300px; height: 300px" id="g-signin2"></div>
</div>
</body>

<script>

    function load() {
        console.log("loaded")
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
