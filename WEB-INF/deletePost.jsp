<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Delete Post</title>
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
    <h1>Delete Post</h1>

        <%
            if(request.getAttribute("errorMessage") == null) {
        %>
            <p>Successful Post Elimination<p>
        <%
            } else {
        %>
            <p style="color:red;">${pageContext.request.getAttribute("errorMessage")}<p>
        <%
            }
        %>
    <p><a href="${pageContext.request.contextPath}/ManageUsers">Home</a></p>
</body>
</html>