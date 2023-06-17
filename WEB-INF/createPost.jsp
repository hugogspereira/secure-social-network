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
        input[type=text] {
            width: 500px;
            height: 50px;
        }
        input[type=submit] {
            display: inline-block;
            width: 150px;
        }
    </style>
</head>
<body>
<h1>Create Post</h1>
<form action="/seg-soft/CreatePost?pageId=<%=request.getParameter("pageId")%>" method="post">
    <div>
        <label for="postText">Post Text:</label>
        <input type="text" id="postText" name="postText" required>
    </div>
    <div>
        <br>
        <input type="submit" value="Create Post">
    </div>
</form>
    <p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>
