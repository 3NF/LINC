<%@ page import="static Data.Constraints.CLIENT_ID" %>
<html>

<head>
    <title>LINC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%-- My css --%>
    <link rel="stylesheet" href="Styles/style.css">


    <%-- Google's authorization --%>
    <script src="https://apis.google.com/js/platform.js" async defer gapi_processed="true"></script>
    <meta name="google-signin-client_id"
          content=<%=CLIENT_ID%>>

</head>

<body onload="load()">
<div class="container" style="text-align: center; padding-top: 30vh">
        <form id="signIn" action="GoogleLogin" method="post">
            <input type="hidden" name="auth_code" value="">
        </form>
            <h2>Welcome! Connect With Google To Start using LINC  </h2>
            <br>
            <div style="display: inline-block; width: 300px; height: 300px" id="g-signin2"></div>
</div>
</body>

<script>

    function load()
    {
        console.log("loaded")
        gapi.signin2.render('g-signin2', {
            'scope': 'profile email',
            'width': 240,
            'height': 50,
            'longtitle': true,
            'theme': 'dark',
            'onsuccess': onSucces,
            'onfailure': onFailure
        });
    }

    function onFailure()
    {

    }

    function finalCallback(authResult)
    {
        let code = authResult['code'];
        if(code)
        {
            console.log("code : " + code);
            let form = document.forms[0];
            form.auth_code = code;
            form.submit();
        }



    }

    function onSucces(googleUser)
    {
        let id_token = googleUser.getAuthResponse().id_token;
        let scope = 'https://www.googleapis.com/auth/classroom.coursework.me.readonly https://www.googleapis.com/auth/classroom.courses.readonly';
        let options = new gapi.auth2.SigninOptionsBuilder({'scope': scope});
        googleUser.grantOfflineAccess(options).then(finalCallback)
        let form = document.getElementById("signIn");
    }

</script>

</html>
