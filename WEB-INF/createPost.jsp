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
<h1>Create Post</h1>
    <form action="/seg-soft/CreatePost" method="post">
        <div>
            <%--@declare id="pageid"--%>
            <label for="pageId">PageId:</label>
            <input type="text" id="PageId" name="PageId" required>
        </div>
        <%-- VALERA A PENA CRIAR ESTE FIELD OU METE-SE O CURR TIME ???????????????????????????????????????????????????????????????????? --%>
        <div>
            <%--@declare id="postdate"--%>
            <label for="postDate">Post Date:</label>
            <input type="email" id="postDate" name="postDate" required>
        </div>
        <div>
            <%--@declare id="posttext"--%>
            <label for="postText">Post Text:</label>
            <input type="text" id="postText" name="postText" required>
        </div>
    </form>
    <p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Cancel</a></p>
</body>
</html>
