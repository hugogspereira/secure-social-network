<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="socialNetwork.SN" %>
<%@ page import="socialNetwork.PageObject" %>
<%@ page import="java.util.Iterator" %>

<%
    Iterator<PageObject> pages;
    try {
        SN sn = SN.getInstance();
        pages = sn.getAllPages().iterator();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Social Network</title>
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
<h1>Myspace 2</h1>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
        PageObject current;
        while(pages.hasNext()){
            current = pages.next();
    %>
    <li><a href="<%=request.getContextPath()%>/Page?id=<%=current.getPageId()%>" ><%=current.getUserId()%>'s page</a></li>
    <%
        }
    %>
</ul>

</body>
</html>
