<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.io.IOException" %>
<%@ page import="com.taufeeq.web.dao.GroupDAO" %>
<%@ page import="com.taufeeq.web.dao.GroupDAOImpl" %>
<%@ page session="false" %> 
<%@ page import="com.taufeeq.web.model.Group" %>

<html>
<head>
    <title>Create New Group</title>
</head>
<body>
    <h2>Create New Group</h2>
    <form action="CreateGroupServlet" method="post">
        <label for="groupName">Group Name:</label>
        <input type="text" id="groupName" name="groupName" required>
        <input type="hidden" name="userId" value="<%= request.getAttribute("userId") %>">
        <button type="submit">Create Group</button>
    </form>
        <form action="dashboard.jsp" method="get">
        <button type="submit">Back to Dashboard</button>
    </form>
</body>
</html>
