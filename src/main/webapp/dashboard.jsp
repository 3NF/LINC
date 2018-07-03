<%@ page import="static Data.Constraints.USER_ID_TOKEN" %>
<%@ page import="static Data.Constraints.COURSE_ID" %><%--
  Created by IntelliJ IDEA.
  User: Luka Tchumburidze
  Date: 6/9/18
  Time: 9:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>LINC Dashboard</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="Styles/dashboard.css">
    <!-- Include Bootstrap -->
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">


    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <!-- Popper JS -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>

<body>
<div class="fill">
    <i class="glyphicon si-glyph-two-arrow-right" id = "panel-swipe-left"></i>
</div>

<script>
    $.ajax({
        type: "POST",
        url: "add-assignment",
        contentType: "application/json", // NOT dataType!
        data: JSON.stringify({

            "user_id_token" : "-",
            "room-id" : "-",
            "assignment-id" : "-",


            "assignment": [
                {
                    "file-name": "sample.h",
                    "content": "asdasd"
                },
                {
                    "file-name": "main.h",
                    "content": "asdasd"
                },
                {
                    "file-name": "main.c",
                    "content": "asdasd"
                },
                {
                    "file-name": "sample.h",
                    "content": "asdasd"
                }
            ]
        }),
        success: function(response) {
            console.log(response);
        }
    });

</script>
</body>
</html>
