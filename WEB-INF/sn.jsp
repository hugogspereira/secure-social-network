<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="socialNetwork.SN" %>
<%@ page import="socialNetwork.PageObject" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="acc.Acc" %>
<%@ page import="socialNetwork.FState" %>
<%@ page import="java.util.List" %>

<%
    Iterator<PageObject> pages;
    SN sn;
    try {
        sn = SN.getInstance();
        pages = sn.getAllPages().iterator();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Feed (Social Network)</title>
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
<h1>Pages</h1>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>

<ul>
    <%
        PageObject current;
        String pathInfo =  request.getPathInfo();
        String pageIdValue = null;
        if (pathInfo != null && pathInfo.length() > 1) {
            pageIdValue = pathInfo.substring(1).split("-")[1];

            int i = 1;
            while(pages.hasNext()){
                current = pages.next();

                if(sn.getfollow(Integer.parseInt(pageIdValue), current.getPageId()).equals(FState.NONE)) {
    %>
    <li><a href="<%=request.getContextPath()%>/Page-=<%=current.getPageId()%>"> Page <%=i%> (<%=current.getUserId()%>'s page)         |           FOLLOW</a></li>
    <%
    }
    else if(sn.getfollow(Integer.parseInt(pageIdValue), current.getPageId()).equals(FState.PENDING)) {
    %>
    <li><a href="<%=request.getContextPath()%>/Page-=<%=current.getPageId()%>"> Page <%=i%> (<%=current.getUserId()%>'s page)         |           PENDING</a></li>
    <%
    }
    else {
    %>
    <li><a href="<%=request.getContextPath()%>/Page-=<%=current.getPageId()%>"> Page <%=i%> (<%=current.getUserId()%>'s page)         |           UNFOLLOW</a></li>
    <%
                }
                i++;
            }
        }
    %>
</ul>

</body>
</html>
