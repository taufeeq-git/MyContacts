<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.taufeeq.web.model.Group" %>
<%@ page import="java.util.List" %>

<%@ page import="com.taufeeq.web.dao.*" %>
<%@ page import="com.taufeeq.web.model.*" %>
<%@ page session="true" %>
<%@ include file="clearcache.jsp" %>
<!DOCTYPE html>

<% int userId=(int)session.getAttribute("userId");
UserDAOImpl user1=new UserDAOImpl();
User user= user1.getUserById(userId);
ContactDAOImpl contactDAO = new ContactDAOImpl();
List<Contact> contactList = contactDAO.getContactsByUserId(userId); 
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
</head>
<body>
    <h1>Welcome,<%=user.getUsername() %></h1>

    <form action="profile.jsp" method="get">
        <button type="submit">Go to Profile</button>
    </form>
    
    <form action="addcontact.jsp" method="get">
        <button type="submit">Add Contact</button>
    </form>
    <br><br><br>
    <form action="creategroup.jsp" method="get">
    <button type="submit">Create New Group</button>
</form>

<%
    List<Group> groupList = (List<Group>) request.getAttribute("groupList");
%>

<h2>Your Groups</h2>
<ul>
    <% if (groupList != null && !groupList.isEmpty()) {
        for (Group group : groupList) { %>
            <li>
                <a href="ViewGroupServlet?groupId=<%= group.getGroupId() %>">
                    <%= group.getGroupName() %>
                </a>
            </li>
    <%  }
    } else { %>
        <li>No groups found.</li>
    <% } %>
</ul>
</ul>



     <h2>Your Contacts:</h2>
    <ul>
        <% for (Contact contact : contactList) { %>
            <li>
               <form action="ViewContactServlet" method="post">
    <input type="hidden" name="contactId" value="<%= contact.getContactId() %>">
    <button type="submit"><%= contact.getUsername() %></button>
</form>

            </li>
        <% } %>
    </ul>

    <form action="dashboard" method="post">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Logout</button>
    </form>
    
</body>
</html>
