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
    <%
        socialNetwork.SN sn;
        try {
            sn = socialNetwork.SN.getInstance();

    %>
    <li><a href="<%=request.getContextPath()%>/ChangePassword">Change Password</a></li>
    <li><a href="<%=request.getContextPath()%>/Logout">Logout</a></li>
    <%
        for (socialNetwork.PageObject curPage: sn.getPages(request.getParameter("username"))) {
    %>
    <li></li>
    <li><a href="<%=request.getContextPath()%>/SocialNetwork?pageId=<%=curPage.getPageId()%>">Feed (Social Network)</a></li>
    <li><a href="<%=request.getContextPath()%>/Page?pageId=<%=curPage.getPageId()%>">Page <%=curPage.getPageId()%></a></li>
    <%
        }
    %>
    <li></li>
    <li><a href="<%=request.getContextPath()%>/CreatePage">Create Page</a></li>
    <li><a href="<%=request.getContextPath()%>/DeletePage">Delete Page</a></li>
    <%
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    %>
</ul>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
</body>
</html>