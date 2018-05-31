<%@ page contentType="text/html;charset=UTF-8" %>
<html>

<head>
    <title>LINC</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%--bootstrap--%>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
    <link href="Styles/assets/css/font-awesome.css" rel="stylesheet">
    <link href="Styles/assets/css/docs.css" rel="stylesheet">
    <link href="Styles/bootstrap-social.css" rel="stylesheet">

    <%--my css--%>
    <link rel="stylesheet" href="Styles/style.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
<div class="container">
    <form id="form_login">
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
            <input id="email" type="text" class="form-control" name="email" placeholder="Email">
        </div>
        <div class="input-group">
            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
            <input id="password" type="password" class="form-control" name="password" placeholder="Password">
        </div>
        <button type="button" id="login-page-login-button" class="btn btn-primary" onclick="loginWithPassword()">Login
        </button>
        <br>
        <br>
        <a class="btn btn-block btn-social btn-lg btn-google" onclick="loginWithGoogle()"><i class="fa fa-google"></i>Sign
            in with Google</a>
    </form>
</div>
</body>

<script>
    function loginWithPassword() {
        let email = $("#email").val();
        let pass = $("#password").val();
        $.ajax({
            type: 'POST',
            data: {email:email, password:pass},
            url:"LoginCheck",
            success: function (result) {
                console.log(result);
            }
        });
    }

    function loginWithGoogl() {

    }
</script>

</html>
