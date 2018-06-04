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
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <meta name="google-signin-client_id"
          content="108555998588-rcq9m8lel3d81vk93othgsg2tolfk9b9.apps.googleusercontent.com">

</head>

<body>
<div class="container">

    <form id="form_login" action="LoginCheck" method="post">
        <div hidden id="wrong-pass" class="alert alert-danger">
            Incorrect email or password.
        </div>
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
            <input id="email" type="email" class="form-control" name="email" placeholder="Email">
        </div>
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
            <input id="password" type="password" class="form-control" name="password" placeholder="Password">
        </div>
        <input type="submit" id="login-page-login-button" class="btn btn-primary" value="Login">
        <br>
        <a href="Register">Don't Have account? Register now!</a>
        <br>
        <br>
        <div class="g-signin2" data-onsuccess="onSignIn"></div>
    </form>
</div>
</body>

<script>
    let wrongPass = <%= (request.getAttribute("wrongPassword") != null) %>;
    function checkWrong() {
        if (wrongPass) {
            $('#wrong-pass').show();
        }
    }
    checkWrong();

    function onSignIn(googleUser) {
        let id_token = googleUser.getAuthResponse().id_token;
        $.post("/GoogleLogin", {id_token: id_token}, function (data) {
            console.log(data);
            switch (data) {
                case "success": {
                    window.location.href = "user/dashboard.jsp";
                    break;
                }
                default: {
                    wrongPass = true;
                    checkWrong();
                    gapi.auth2.getAuthInstance().disconnect();
                }
            }
        });
    }
</script>

</html>
