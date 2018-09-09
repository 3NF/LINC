<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><%=(request.getAttribute("code") != null ?  request.getAttribute("code") : "Unknown") + " Error"%></title>
</head>
<body>
    <h1>LINC Team is saying hi from the error page!</h1>
    <p><%=request.getAttribute("message") != null ? request.getAttribute("message") : "There was some unknown error, probably it's your fault..."%></p>
    <img src="https://files.slack.com/files-tmb/T76SV5WBX-FAU6ZUZL6-17e77bac98/img_20180423_111622_1024.jpg">
</body>
</html>
