<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="socialNetwork.SN" %>
<%@ page import="socialNetwork.PageObject" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="socialNetwork.FState" %>

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
        String pageId =  request.getParameter("pageId");
        if (pageId != null) {
            while(pages.hasNext()){
                current = pages.next();

                if(!sn.getPage(Integer.parseInt(pageId)).getUserId().equals(current.getUserId())) {
                    FState state = sn.getfollowState(Integer.parseInt(pageId), current.getPageId());
                    if(state == null || state.equals(FState.NONE)) {
    %>
    <li>
        <a href="<%=request.getContextPath()%>/Page?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>"> Page <%=current.getPageId()%> (<%=current.getUserId()%>'s page)</a>
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="<%= request.getContextPath() %>/Follow?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>">FOLLOW</a>
    </li>
    <%
    }
    else if(state.equals(FState.PENDING)) {
    %>
    <li>
        <a href="<%=request.getContextPath()%>/Page?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>"> Page <%=current.getPageId()%> (<%=current.getUserId()%>'s page)</a>
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="<%= request.getContextPath() %>/Unfollow?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>">PENDING</a>
    </li>
    <%
    }
    else {
    %>
    <li>
        <a href="<%=request.getContextPath()%>/Page?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>"> Page <%=current.getPageId()%> (<%=current.getUserId()%>'s page)</a>
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="<%= request.getContextPath() %>/Unfollow?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>">UNFOLLOW</a>
    </li>
    <%
        }
    }
    else {
    %>
    <li>
        <a href="<%=request.getContextPath()%>/Page?pageId=<%=pageId%>&visitedPageId=<%=current.getPageId()%>"> Page <%=current.getPageId()%> (<%=current.getUserId()%>'s page)</a>
        &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;
        OWNER
    </li>
    <%
                }
            }
        }
    %>
</ul>
<p><a href="${pageContext.request.contextPath}/ManageUsers">Home</a></p>

</body>
</html>
