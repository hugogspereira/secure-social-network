<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Authenticate User Page</title>
</head>
<body>
    <h1>Login</h1>
    <form action="/seg-soft/AuthenticateUser" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>
        <br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>
        <br><br>

        <input type="submit" value="Submit">
    </form>
</body>
</html>