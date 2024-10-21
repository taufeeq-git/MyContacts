<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.*" %>
<%@ page import="com.taufeeq.web.model.Group" %>
<%@ page import="java.util.List" %>
<%@ page import="com.taufeeq.web.dao.*" %>
<%@ page import="com.taufeeq.web.model.*" %>
<%@ page session="false" %> 
<%@ include file="clearcache.jsp" %>

<!DOCTYPE html>
<%
   /* String sessionId = null;
    
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("sessionId".equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }
    }

   
    SessionDAOImpl sessionDAO = new SessionDAOImpl();
    UserDAOImpl userDAO = new UserDAOImpl();
    Integer userId = sessionDAO.getUserIdFromSession(sessionId); 
    */
    String selectedFormat = null;
    int userId=(int) request.getAttribute("userId");
    UserDAO userDAO= new UserDAOImpl();
    selectedFormat= userDAO.getFormat(userId);
    
   
    if (selectedFormat == null) {
        selectedFormat = "%Y-%m-%d %H:%i:%s"; 
    }
  
    User user = userDAO.getUserById(userId);
    ContactDAOImpl contactDAO = new ContactDAOImpl();
    List<Contact> contactList = contactDAO.getContactsByUserId(userId);

   
    GroupDAO groupDAO = new GroupDAOImpl();
    List<Group> groupList = groupDAO.getUserGroupsWithIds(userId);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Dashboard</title>
</head>
<body>
    <h1>Welcome, <%= user.getUsername() %></h1>

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
    
    

    <h2>Select Date-Time Format:</h2>
    <form action="dashboard" method="post">
        <input type="hidden" name="action" value="setFormat">
        <select name="dateFormat">
            <option value="%Y-%m-%d %H:%i:%s" <%= selectedFormat.equals("%Y-%m-%d %H:%i:%s") ? "selected" : "" %>>YYYY-MM-DD HH:MM:SS</option>
            <option value="%d-%m-%Y %H:%i" <%= selectedFormat.equals("%d-%m-%Y %H:%i") ? "selected" : "" %>>DD-MM-YYYY HH:MM</option>
            <option value="%m-%Y %H" <%= selectedFormat.equals("%m-%Y %H") ? "selected" : "" %>>MM-YYYY HH</option>
        </select>
        <button type="submit">Set Format</button>
    </form>
    

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

    <h2>Your Contacts:</h2>
    <ul>
        <% if (contactList != null && !contactList.isEmpty()) {
            for (Contact contact : contactList) { %>
                <li>
                    <form action="ViewContactServlet" method="post">
                        <input type="hidden" name="contactId" value="<%= contact.getContactId() %>">
                        <button type="submit"><%= contact.getUsername() %></button>
                    </form>
                </li>
            <% }
        } else { %>
            <li>No contacts found.</li>
        <% } %>
    </ul>

    <form action="dashboard" method="post">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Logout</button>
    </form>
    
</body>
</html>
