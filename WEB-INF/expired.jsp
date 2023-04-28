<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Logout Page</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, Helvetica, sans-serif;
        }
        label {
            display: inline-block;
            width: 150px;
            text-align: right;
            margin-right: 10px;
        }
    </style>
</head>
<body>
<h1>Who are you?</h1>
<p>It seams that your session has expired or is invalid.</p>
<p><a href="${pageContext.request.contextPath}/AuthenticateUser">Back to Login Page</a></p>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
</body>
</html>