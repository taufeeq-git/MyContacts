<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.taufeeq.web.dao.*" %>
<%@ include file="clearcache.jsp" %>
<%@ page import="com.taufeeq.web.model.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page session="false" %>  
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.taufeeq.web.scheduler.*" %>
<%@ page session="false" %>

<%
int userId=(int) request.getAttribute("userId");
UserDAOImpl user1 = new UserDAOImpl();
User user = user1.getUserById(userId);


%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
</head>
<body>
    <h1>Edit Your Profile</h1>

    <form action="editProfile" method="post">
        <input type="hidden" name="userId" value="<%= user.getUserId() %>">

        <label for="username">Username:</label>
        <input type="text" name="username" value="<%= user.getUsername() %>" required><br>

        <label for="gender">Gender:</label>
        <input type="text" name="gender" value="<%= user.getGender() %>" required><br>

        <label for="birthday">Birthday:</label>
        <input type="date" name="birthday" value="<%= user.getBirthday() != null ? new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()) : "" %>"><br>

        <label for="location">Location:</label>
        <input type="text" name="location" value="<%= user.getLocation() %>" required><br><br>

        <button type="submit">Save Changes</button>
    </form>

    <h2>Emails</h2>
    <ul>
        <%
        List<Email> emails = user.getEmails();
        if (emails != null && !emails.isEmpty()) {
            for (Email email : emails) {
        %>
        <li>
            <%= email.getEmail() %> 
            <form action="editProfile?action=deleteEmail" method="post" style="display:inline;">
                <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                <input type="hidden" name="email" value="<%= email.getEmail() %>">
                <button type="submit">Delete</button>
            </form>
        </li>
        <%
            }
        }
        %>
    </ul>

    <h2>Phone Numbers</h2>
    <ul>
        <%
        List<PhoneNumber> phoneNumbers = user.getPhoneNumbers();
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            for (PhoneNumber phoneNumber : phoneNumbers) {
        %>
        <li>
            <%= phoneNumber.getPhoneNumber() %> 
            <form action="editProfile?action=deletePhoneNumber" method="post" style="display:inline;">
                <input type="hidden" name="userId" value="<%= user.getUserId() %>">
                <input type="hidden" name="phoneNumber" value="<%= phoneNumber.getPhoneNumber() %>">
                <button type="submit">Delete</button>
            </form>
        </li>
        <%
            }
        }
        %>
    </ul>

    <h2>Add Email</h2>
    <form action="editProfile?action=addEmail" method="post">
        <input type="email" name="email" placeholder="Enter alternate email" required>
        <button type="submit">Add Email</button>
    </form>

    <h2>Add Phone Number</h2>
    <form action="editProfile?action=addPhoneNumber" method="post">
        <input type="tel" name="number" placeholder="Enter phone number" required>
        <button type="submit">Add Phone Number</button>
    </form>

    <a href="profile.jsp">Back to Profile</a>
</body>
</html>
