<%@ page import="socialNetwork.PageObject" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="socialNetwork.SN" %>
<%@ page import="socialNetwork.PostObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    SN sn;
    try {
        sn = SN.getInstance();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Post</title>
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
        .flex-parent {
            display: flex;
        }
        .flex-child {
            flex: 1;
        }
    </style>
</head>
<body>
<h1>Post:</h1>

<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
    String postId = request.getParameter("postId");
    if (postId != null) {
        PostObject post = sn.getPost(Integer.parseInt(postId));
    %>
    <br>
    <li>Post ID:<br>&nbsp;&nbsp;&nbsp;&nbsp; <%= post.getPostId() %></li>
    <br>
    <li>Post Date:<br>&nbsp;&nbsp;&nbsp;&nbsp; <%= post.getPostDate() %></li>
    <br>
    <li>Post Content:<br>&nbsp;&nbsp;&nbsp;&nbsp; <%= post.getPostText() %></li>
    <%
        }
    %>
</ul>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Home</a></p>

</body>
</html>
