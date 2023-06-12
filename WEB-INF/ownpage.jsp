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
    <title>Own Page</title>
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
<h1>Page ${pageContext.request.getParameter("id")}</h1>

<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
        String pageId = request.getParameter("pageId");
        if (pageId != null) {
    %>
    <br>
    <li><a href="<%=request.getContextPath()%>/CreatePost?pageId=<%=pageId%>">Create Post</a></li>
    <br>
    <br>
    <%
        for (PostObject postObject: sn.getPagePosts(Integer.parseInt(pageId))) {

    %>
    <li>
        <a href="<%= request.getContextPath() %>/Post?postId=<%= postObject.getPostId() %>&visiterPageId=<%=pageId%>">Post <%=postObject.getPostId()%></a>
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <%= sn.getLikes(postObject.getPostId()).size() %> (Likes)
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="<%= request.getContextPath() %>/DeletePost?postId=<%= postObject.getPostId() %>">DELETE</a>
    </li>
    <%
        }
        %>
    <br>
    <br>
    <li><a href="<%=request.getContextPath()%>/Followers?pageId=<%=pageId%>">Followers</a></li>
    <br>
    <li><a href="<%=request.getContextPath()%>/FollowersRequests?pageId=<%=pageId%>">Follow Requests</a></li>
    <%
    }
    %>
</ul>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Home</a></p>
</body>
</html>
