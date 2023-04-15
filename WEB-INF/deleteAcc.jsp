<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Delete Account</title>
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
    <h1>Delete Account</h1>
    <p>Please enter the username of the account you wish to delete:</p>
<form action="/seg-soft/DeleteAccount" method="POST">
    <div>
        <label for="username">Username:</label>
        <input type="text" id="username" name="username">
        </div>
    <div>
        <br>
        <input type="submit" value="Delete">
    </div>
</form>
    <p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>