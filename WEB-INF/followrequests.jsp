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
    <title>Follow Requests</title>
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
<h1>Pages</h1>

<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
        String pathInfo = request.getPathInfo();
        String pageIdValue;
        if (pathInfo != null && pathInfo.length() > 1) {
            pageIdValue = pathInfo.substring(1).split("-")[1];
            PageObject pageObject = sn.getPage(Integer.parseInt(pageIdValue));

            for (PageObject curObject: sn.getrequests(Integer.parseInt(pageIdValue))) {

    %>
    <li>
        <a href="<%= request.getContextPath() %>/Page-<%= curObject.getPageId() %>">Page <%= curObject.getPageId() %></a>
        |
        <a href="<%= request.getContextPath() %>/AcceptFollow-<%= pageObject.getPageId() %>-<%= curObject.getPageId() %>">ACCEPT</a>
        |
        <a href="<%= request.getContextPath() %>/DeclineFollow-<%= pageObject.getPageId() %>-<%= curObject.getPageId() %>">DECLINE</a>
    </li>
    <%
        }
    }
    %>
</ul>
</body>
</html>
