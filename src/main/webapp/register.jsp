<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link href="Styles/register.css" rel="stylesheet">
<link href="Styles/style.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript" src="JavaScript/register.js"></script>
<link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
<head>
    <title>Sing Up - LINC</title>
</head>
<body>
    <form method="post" action="Register" class="form-group" onsubmit="return checkForm(this)">
        <h2 style="width: 300px">Register to LINC</h2>
        <ul id="alertBox" style="width: 300px; text-align: left"></ul>
        <div class="input-group">
            <input type="text" name="firstName" placeholder="First Name"><br>
            <input type = "text" name="lastName" placeholder="Last Name"><br>
            <input type="email" id=email name="email" placeholder="E-Mail"><br>
            <input type="email" id="confirmEmail" placeholder="Confirm E-Mail"><br>
            <input type="password" id="password" name="password" placeholder="Password"><br>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password"><br>
            <button type="submit" class="btn-primary" style="text-align: center !important;">Register Now!</button>
        </div>
    </form>
</body>
</html>
