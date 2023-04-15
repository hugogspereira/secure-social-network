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
<h1>Change Password</h1>
<form action="/seg-soft/ChangePwd" method="post">
    <div>
        <label for="newPassword1">New Password:</label>
        <input type="password" id="newPassword1" name="newPassword1"  required>
    </div>
    <div>
        <label for="newPassword2">Confirm Password:</label>
        <input type="password" id="newPassword2" name="newPassword2" required>
    </div>
    <div>
        <br>
        <input type="submit" value="Change Password">
    </div>
</form>
<br>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>