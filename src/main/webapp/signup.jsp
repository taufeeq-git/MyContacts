<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="clearcache.jsp" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MyContacts - Sign Up</title>
    
</head>
<body>
    <div class="container">
        <h1>Sign Up for MyContacts</h1>
        <form action="signup" method="post">
            <input type="text" name="username" placeholder="Username" required><br><br>
            <input type="email" name="email" placeholder="Email" required><br><br>
            <input type="password" name="password" placeholder="Password" required><br><br>
            <input type="text" name="number" placeholder="Phone number" pattern="[0-9]{10}" required><br><br>
            <input type="text" name="gender" placeholder="Gender" required><br><br>
            <input type="date" name="dob" placeholder="YYYY-MM-DD(DOB)" required><br><br>
            <input type="text" name="location" placeholder="Location" required><br><br>
            <input type="submit" value="Sign Up">
        </form>
        <div class="login-link">
            <p>Already have an account? <a href="login.jsp">Login</a></p>
        </div>
    </div>
</body>
</html>
