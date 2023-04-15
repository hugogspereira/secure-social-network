<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Account</title>
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
<h1>Create Account</h1>
    <form action="/seg-soft/CreateAccount" method="post">
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div>
            <label for="password1">Password:</label>
            <input type="password" id="password1" name="password1" required>
        </div>
        <div>
            <label for="password2">Confirm Password:</label>
            <input type="password" id="password2" name="password2" required>
        </div>
        <div>
            <br>
            <input type="submit" value="Create Account">
        </div>
    </form>
    <p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>
