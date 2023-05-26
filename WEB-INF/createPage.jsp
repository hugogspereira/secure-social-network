<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Page</title>
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
        input[type=text], input[type=password] {
            display: inline-block;
            width: 200px;
        }
        input[type=submit] {
            display: inline-block;
            width: 150px;
        }
    </style>
</head>
<body>
<h1>Create Page</h1>
    <form action="/seg-soft/CreatePage" method="post">
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>
        </div>
        <div>
            <label for="pageTitle">Page Title:</label>
            <input type="text" id="pageTitle" name="pageTitle" required>
        </div>
        <div>
            <label for="pagePic">Page Picture:</label>
            <input type="text" id="pagePic" name="pagePic" required>
        </div>
        <div>
            <br>
            <input type="submit" value="Create Account">
        </div>
    </form>
    <p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>
