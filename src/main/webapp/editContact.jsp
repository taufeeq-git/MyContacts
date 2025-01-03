<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.taufeeq.web.model.Contact" %>
<%@ page session="false" %>

<%
    Contact contact = (Contact) request.getAttribute("contact");

%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Contact</title>
</head>
<body>
    <h1>Edit Contact</h1>
    <form action="editContact" method="post">
        <input type="hidden" name="contactId" value="<%= contact.getContactId() %>" />


        <label for="username">Name:</label>
        <input type="text" id="username" name="username" value="<%= contact.getUsername() %>" required /><br/>

        <label for="gender">Gender:</label>
        <select id="gender" name="gender">
            <option value="">Not specified</option>
            <option value="Male" <%= "Male".equals(contact.getGender()) ? "selected" : "" %>>Male</option>
            <option value="Female" <%= "Female".equals(contact.getGender()) ? "selected" : "" %>>Female</option>
         
        </select><br/>

        <label for="birthday">Birthday:</label>
        <input type="date" id="birthday" name="birthday" value="<%= contact.getBirthday() != null ? contact.getBirthday() : "" %>" /><br/>

        <label for="favorite">Favorite:</label>
        <input type="checkbox" id="favorite" name="favorite" <%= contact.getFavorite() ? "checked" : "" %> /><br/>

        <label for="archive">Archive:</label>
        <input type="checkbox" id="archive" name="archive" <%= contact.getArchive() ? "checked" : "" %> /><br/>

        <button type="submit">Save Changes</button>
    </form>

   <form action="ViewContactServlet" method="get"> 
        <input type="hidden" name="contactId" value="<%= contact.getContactId() %>" />
        <button type="submit">Cancel</button>
    </form>
    
    
</body>
</html>
