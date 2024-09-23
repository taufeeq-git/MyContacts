<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
</head>
<body>
    <h1>Your Profile</h1>
    <p>Username: ${user.username}</p>
    <p>Gender: ${user.gender}</p>
    <p>Birthday: ${user.birthday}</p>
    <p>Location: ${user.location}</p>

    <h2>Emails</h2>
    <ul>
        <c:forEach var="email" items="${user.emails}">
            <li>${email}</li>
        </c:forEach>
    </ul>

    <h2>Phone Numbers</h2>
    <ul>
        <c:forEach var="phone" items="${user.phoneNumbers}">
            <li>${phone}</li>
        </c:forEach>
    </ul>

    <h2>Add Alternate Email</h2>
    <form action="dashboard?action=addEmail" method="post">
        <input type="email" name="email" placeholder="Enter alternate email" required>
        <button type="submit">Add Email</button>
    </form>

    <h2>Add Phone Number</h2>
    <form action="dashboard?action=addPhoneNumber" method="post">
        <input type="tel" name="number" placeholder="Enter phone number" required>
        <button type="submit">Add Phone Number</button>
    </form>
</body>
</html>
