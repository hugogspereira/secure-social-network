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
        input[type=text], input[type=date], input[type=number] {
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
<h1>Create Post</h1>
<form action="/seg-soft/CreatePost" method="post">
    <div>
        <label for="pageId">PageId:</label>
        <input type="number" id="pageId" name="pageId" required>
    </div>
    <div>
        <label for="postDate">Post Date:</label>
        <input type="date" id="postDate" name="postDate" required>
    </div>
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
