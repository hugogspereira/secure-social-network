<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <title>Logout Page</title>
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
<h1>Oops! It seems like you're missing a crucial superpower for this operation.</h1>
<p>Unauthorized access detected. Permission Denied. Remember, with great power comes great access levels!</p>
<p style="color:red;"> ${pageContext.request.getAttribute("errorMessage")} </p>
<input type=button value="Go Back" onCLick="history.back()">
</body>
</html>