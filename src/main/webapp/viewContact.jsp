<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.taufeeq.web.model.Contact" %>
<%@ include file="clearcache.jsp" %>
<%@ page session="false" %>

<% 
    Contact contact = (Contact) request.getAttribute("contact");
	int contactId = (int) request.getAttribute("contactId");
	if(contact == null ) {
       
        response.sendRedirect("dashboard?error=ContactNotFound");
        return;  
    }
    
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Details</title>
</head>
<body>
    <h1>Contact Details for <%= contact.getUsername() %></h1>

    <h2>General Information</h2>
    <p><strong>Name:</strong> <%= contact.getUsername() %></p>
    <p><strong>Gender:</strong> <%= contact.getGender() != null ? contact.getGender() : "Not specified" %></p>
    <p><strong>Birthday:</strong> <%= contact.getBirthday() != null ? contact.getBirthday() : "Not specified" %></p>
    <p><strong>Favorite:</strong> <%= contact.getFavorite() == false? "No":"Yes" %></p>
    <p><strong>Archived:</strong> <%= contact.getArchive() == false? "No":"Yes"%></p>
    <p><strong>Created Time:</strong> <%= contact.getFormattedCreatedTime() != null ? contact.getFormattedCreatedTime() : "Not available" %></p> 
    
   

    <h2>Emails</h2>
    <ul>
        <% 
            if (contact.getEmails() != null && !contact.getEmails().isEmpty()) {
                for (String email : contact.getEmails()) { 
        %>
                    <li><%= email %></li>
        <% 
                } 
            } else { 
        %>
            <li>No email addresses available</li>
        <% 
            } 
        %>
    </ul>

   <h2>Add Alternate Email</h2>
<form action="ViewContactServlet" method="post">
    <input type="hidden" name="contactId" value="${contactId}" />
    <input type="email" name="newEmail" placeholder="Enter alternate email" required />
    <input type="hidden" name="action" value="addEmail" />
    <button type="submit">Add Email</button>
</form>

    <h2>Phone Numbers</h2>
    <ul>
        <% 
            if (contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty()) {
                for (String phone : contact.getPhoneNumbers()) { 
        %>
                    <li><%= phone %></li>
        <% 
                } 
            } else { 
        %>
            <li>No phone numbers available</li>
        <% 
            } 
        %>
    </ul>

 <h2>Add Alternate Phone Number</h2>
<form action="ViewContactServlet" method="post">
    <input type="hidden" name="contactId" value="${contactId}" />
    <input type="tel" name="newPhoneNumber" placeholder="Enter alternate phone number" required />
    <input type="hidden" name="action" value="addPhoneNumber" />
    <button type="submit">Add Phone Number</button>
</form>
<br><br>

<form action="editContact" method="get">
    <input type="hidden" name="contactId" value="${contactId}" />
    <button type="submit">Edit Contact</button>
</form>

<br><br>
<h2>Delete Contact</h2>
<form action="ViewContactServlet" method="post">
    <input type="hidden" name="contactId" value="${contactId}" />
    <input type="hidden" name="action" value="deleteContact" />
    <button type="submit" onclick="return confirm('Are you sure you want to delete this contact?');">Delete Contact</button>
</form>
<br><br>
    
<br><br>
        <form action="dashboard" method="get">
        <button type="submit">Back to Dashboard</button>
    </form>
</body>
</html>
