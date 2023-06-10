<%@ page import="acc.Acc" %>
<%@ page import="auth.Authenticator" %>
<%@ page import="socialNetwork.SN" %>
<%@ page import="socialNetwork.PageObject" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Acc authUser = Authenticator.getInstance().checkAuthenticatedRequest(request, response);
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
    <li><a href="<%=request.getContextPath()%>/ChangePassword">Change Password</a></li>
    <li><a href="<%=request.getContextPath()%>/Logout">Logout</a></li>
    <%
        for (PageObject curPage: sn.getPages(authUser.getAccountName())) {
    %>
    <li></li>
    <li><a href="<%=request.getContextPath()%>/SocialNetwork-<%=curPage.getPageId()%>">Feed (Social Network)</a></li>
    <li><a href="<%=request.getContextPath()%>/Page-<%=curPage.getPageId()%>">Page <%=curPage.getPageId()%></a></li>
    <%
        }
    %>
</ul>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
</body>
</html>