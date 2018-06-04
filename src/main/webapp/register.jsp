<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link href="Styles/register.css" rel="stylesheet">
<link href="Styles/style.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link href="Styles/assets/css/bootstrap.css" rel="stylesheet">
<head>
    <title>Sing Up - LINC</title>
</head>
<body>
    <form method="post" action="Register" class="form-group">
        <h2>Register to LINC</h2>
        <div class="input-group">
            <input type="text" name="firstName" placeholder="First Name"><br>
            <input type = "text" name="lastName" placeholder="Last Name"><br>
            <input type="email" id=email name="email" placeholder="E-Mail"><br>
            <input type="email" id="confirmEmail" placeholder="Confirm E-Mail"><br>
            <input type="password" id="password" name="password" placeholder="Password"><br>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password"><br>
            <input type="submit" class="btn-primary" value="Register Now!">
        </div>
    </form>
</body>
</html>
