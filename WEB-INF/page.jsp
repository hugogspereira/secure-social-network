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
    <title>Page</title>
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
<h1>Page Posts:</h1>

<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
    String pageId = request.getParameter("pageId");
    String visiterPageId = request.getParameter("visiterPageId");
    if (pageId != null) {
         for (PostObject postObject: sn.getPagePosts(Integer.parseInt(pageId))) {

    %>
    <li>
        <a href="<%= request.getContextPath() %>/Post?postId=<%= postObject.getPostId() %>&visiterPageId=<%=visiterPageId%>">Post <%=postObject.getPostId()%></a>
        |
        <%
            if( sn.isLiked(postObject.getPostId(), Integer.parseInt(visiterPageId)) ) {
        %>
        <a href="<%= request.getContextPath() %>/Unlike?postId=<%= postObject.getPostId() %>&visiterPageId=<%=visiterPageId%>">UNLIKE</a>
        <%
            }
            else {
        %>
        <a href="<%= request.getContextPath() %>/Like?postId=<%= postObject.getPostId() %>&visiterPageId=<%=visiterPageId%>">LIKE</a>
        <%
            }
        }
    }
    %>
    </li>
</ul>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Home</a></p>

</body>
</html>
