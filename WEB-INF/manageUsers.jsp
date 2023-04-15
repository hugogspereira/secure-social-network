<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Users Page</title>
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
    <h1>Manage Users</h1>
    <ul>
        <li><a href="<%=request.getContextPath()%>/CreateUser">Create User</a></li>
        <li><a href="<%=request.getContextPath()%>/DeleteUser">Delete User</a></li>
        <li><a href="<%=request.getContextPath()%>/ChangePassword">Change Password</a></li>
        <li><a href="<%=request.getContextPath()%>/Logout">Logout</a></li>
    </ul>
</body>
</html>