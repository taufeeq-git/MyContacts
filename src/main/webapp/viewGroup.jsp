<%@ page import="java.util.List" %>
<%@ include file="clearcache.jsp" %>
<%@ page import="com.taufeeq.web.model.Contact" %>
<%@ page session="false" %> 

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>View Group</title>
</head>
<body>
<% List<Contact> groupContacts = (List<Contact>) request.getAttribute("groupContacts"); %>

<h1>Contacts of group: <%= request.getAttribute("groupName") %></h1>

<ul>
    <% 
        if (groupContacts != null && !groupContacts.isEmpty()) {
            for (Contact contact : groupContacts) { 
    %>
        <li>
            <form action="ViewContactServlet" method="post" style="display:inline;">
                <input type="hidden" name="contactId" value="<%= contact.getContactId() %>">
                <button type="submit"><%= contact.getUsername() %></button>
            </form>

            <form action="AddContactToGroupServlet" method="post" style="display:inline;">
                <input type="hidden" name="contactId" value="<%= contact.getContactId() %>">
                <input type="hidden" name="groupId" value="<%= request.getAttribute("groupId") %>">
                <input type="hidden" name="action" value="delete">
                <button type="submit" class="btn btn-danger">Delete</button>
            </form>
        </li>
    <%
            }
        } else { 
    %>
        <li>No contacts in this group.</li>
    <%
        } 
    %>
</ul>

<h2>Add a Contact to this Group</h2>
<form action="AddContactToGroupServlet" method="post">
    <input type="hidden" name="groupId" value="<%= request.getAttribute("groupId") %>">
    <input type="hidden" name="action" value="add">

    <select name="contactId">
        <option value="">-- Select a contact --</option>
        <% 
            List<Contact> userContacts = (List<Contact>) request.getAttribute("userContacts");
            if (userContacts != null && !userContacts.isEmpty()) {
                for (Contact contact : userContacts) { 
        %>
                    <option value="<%= contact.getContactId() %>"><%= contact.getUsername() %></option>
        <%
                }
            } else {
        %>
            <option value="">No contacts available</option>
        <%
            }
        %>
    </select>

    <button type="submit">Add Contact</button>
</form>

<br><br>
<form action="dashboard" method="get">
    <button type="submit">Back to Dashboard</button>
</form>

</body>
</html>
