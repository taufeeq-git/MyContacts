<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.taufeeq.web.dao.*" %>
<%@ include file="clearcache.jsp" %>
<%@ page import="com.taufeeq.web.model.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page session="false" %>  
<%@ page import="com.taufeeq.web.scheduler.*" %>
<%@ page session="false" %>

<%

Cookie[] cookies = request.getCookies();
String sessionId = null;
if (cookies != null) {
    for (Cookie cookie : cookies) {
        if ("sessionId".equals(cookie.getName())) {
            sessionId = cookie.getValue();
            break;
        }
    }
}

/*
SessionDAOImpl sessionDAO = new SessionDAOImpl();
boolean validSession = sessionDAO.isValidSession(sessionId);

if (sessionId == null || !validSession) {
 
    System.out.println("no sessionid");
    response.sendRedirect("login.jsp");
}
else
{
*/
	int userId=(int) request.getAttribute("userId");
   
    SessionScheduler sessionScheduler = (SessionScheduler) getServletContext().getAttribute("sessionScheduler");
    sessionScheduler.addSession(sessionId, new Timestamp(System.currentTimeMillis()));
  
   
    UserDAOImpl user1 = new UserDAOImpl();
    User user = user1.getUserById(userId);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
</head>
<body>
    <h1>Your Profile</h1>
    <p>Username: <%= user.getUsername() %></p>
    <p>Gender: <%= user.getGender() %></p>
    <p>Birthday: <%= user.getBirthday() %></p>
    <p>Location: <%= user.getLocation() %></p>

    <h2>Emails</h2>
    <ul>
    	<%
    	for (String email: user.getEmails()){
    		if(email!=null){%>
    		<li><%= email %></li>
    		
    <% 	}}
    	%>
    </ul>

    <h2>Phone Numbers</h2>
    <ul>
    	<%
    	for (String ph: user.getPhoneNumbers()){
    		if(ph!=null){%>
    		<li><%= ph %></li>
    		
    <% 	}}
    	%>
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
    </form> <br><br>
    
    <form action="dashboard" method="get">
        <button type="submit">Back to Dashboard</button>
    </form>
</body>
</html>

<%  %>
