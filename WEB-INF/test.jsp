<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Change Password Page</title>
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
        input[type=password] {
            display: inline-block;
            width: 200px;
        }
    </style>
</head>
<body>
<h1>The Counter App!</h1>
<h1>${pageContext.request.getAttribute("counter")}</h1>
<form action="/seg-soft/Test" method="post">
    <div>
        <input type=submit value="Increment" name=increment>
    </div>
</form>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>