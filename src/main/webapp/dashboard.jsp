<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
</head>
<body>
    <h1>Welcome, ${user.username}!</h1>

    <form action="profile.jsp" method="get">
        <button type="submit">Go to Profile</button>
    </form>

    <form action="dashboard" method="post">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Logout</button>
    </form>
</body>
</html>
