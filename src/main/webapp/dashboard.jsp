<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taufeeq.web.model.*"%>
<%@ page import="com.taufeeq.web.cache.*"%>
<%@ page import="java.util.List"%>
<%@ page import="com.taufeeq.web.dao.*"%>
<%@ page import="com.taufeeq.web.model.*"%>
<%@ page session="false"%>
<%@ include file="clearcache.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>User Dashboard</title>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap"
	rel="stylesheet">
<style>
:root {
	/* Primary Colors */
	--pink-primary: #fbe3e8;
	--pink-light: #fef1f3;
	--pink-dark: #f8d5dc;
	--blue-green-primary: #5cbdb9;
	--blue-green-light: #6fcac6;
	--blue-green-dark: #4ea7a3;
	--teeny-green-primary: #ebf6f5;
	--teeny-green-light: #f5fafa;
	--teeny-green-dark: #e0efee;
	--text-primary: #2c3e50;
	--text-secondary: #34495e;
	--text-light: #ffffff;
	--shadow-subtle: rgba(92, 189, 185, 0.1);
}

* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: 'Inter', sans-serif;
	background: var(--teeny-green-primary);
	color: var(--text-primary);
	line-height: 1.6;
	min-height: 100vh;
}

.dashboard-wrapper {
	width: 100%;
	padding: 20px;
	max-width: 1400px;
	margin: 0 auto;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
}

.header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 25px;
	border-radius: 16px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 30px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	transition: transform 0.3s ease;
}

.header:hover {
	transform: translateY(-2px);
}

.header h1 {
	font-size: 1.8rem;
	font-weight: 600;
	text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
}

.btn {
	background: var(--pink-primary);
	color: var(--text-primary);
	border: none;
	padding: 12px 24px;
	border-radius: 12px;
	cursor: pointer;
	font-weight: 500;
	transition: all 0.3s ease;
	box-shadow: 0 4px 6px var(--shadow-subtle);
	text-decoration: none;
	display: inline-block;
	width: 100%;
}

.btn:hover {
	background: var(--pink-light);
	transform: translateY(-2px);
	box-shadow: 0 6px 12px var(--shadow-subtle);
}

.main-content {
	display: flex;
	gap: 24px;
	flex: 1;
}

.lists-container {
	flex-grow: 1;
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 24px;
	height: calc(100vh - 150px);
	/
}

.dashboard-section {
	background: var(--teeny-green-light);
	border-radius: 16px;
	padding: 30px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	position: relative;
	transition: transform 0.3s ease;
	height: 100%;
	display: flex;
	flex-direction: column;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.section-header h2 {
	color: var(--blue-green-primary);
	margin-bottom: 0;
	font-weight: 600;
	position: relative;
	z-index: 1;
}

.list-section-content {
	margin-bottom: 15px;
	overflow-y: auto;
	padding-right: 10px;
	flex: 1;
	max-height: calc(100vh - 250px);
}
/* Scrollbar Styling */
.list-section-content::-webkit-scrollbar {
	width: 8px;
}

.list-section-content::-webkit-scrollbar-track {
	background: var(--teeny-green-light);
	border-radius: 10px;
}

.list-section-content::-webkit-scrollbar-thumb {
	background: var(--blue-green-light);
	border-radius: 10px;
}

.list-section-content::-webkit-scrollbar-thumb:hover {
	background: var(--blue-green-primary);
}

/* For Firefox */
.list-section-content {
	scrollbar-width: thin;
	scrollbar-color: var(--blue-green-light) var(--teeny-green-light);
}

/* Ensure list items maintain their style */
.list-items li:last-child {
	margin-bottom: 0;
}

.list-items {
	list-style: none;
	width: 100%;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;
	gap: 1rem;
	
}

.list-items > form {
	width: 100%;
}

.list-items > form > .btn {
	padding : 20px;
	text-align : left;
	font-size : 18px;

}

.list-items li {
	background: var(--pink-light);
	margin-bottom: 12px;
	padding: 14px 18px;
	border-radius: 12px;
	transition: all 0.3s ease;
}

.list-items li:hover {
	background: var(--pink-primary);
	transform: translateX(5px);
}

.format-container {
	width: 320px;
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.format-selector {
	background: var(--blue-green-light);
	border-radius: 16px;
	padding: 25px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
}

.format-selector h3 {
	color: var(--text-light);
	margin-bottom: 15px;
	text-align: center;
}

.format-selector select {
	width: 100%;
	padding: 12px;
	border-radius: 10px;
	border: 2px solid var(--teeny-green-primary);
	background: var(--teeny-green-light);
	margin-bottom: 15px;
	color: var(--text-primary);
	font-size: 0.95rem;
}

.sync-contacts-section {
	background: var(--teeny-green-light);
	border-radius: 16px;
	padding: 20px;
	text-align: center;
	box-shadow: 0 8px 16px var(--shadow-subtle);
}

.sync-message {
	margin-top: 20px;
	padding: 16px;
	border-radius: 12px;
	background: var(--blue-green-light);
	color: var(--text-light);
}

.sync-message.error {
	background: var(--pink-dark);
}

.logout-section {
	background: var(--teeny-green-light);
	border-radius: 16px;
	padding: 20px;
	text-align: center;
	box-shadow: 0 8px 16px var(--shadow-subtle);
}

.logout-section .btn {
	background: var(--pink-dark);
}

.logout-section .btn:hover {
	background: var(--pink-primary);
}

@media ( max-width : 1200px) {
	.main-content {
		flex-direction: column;
	}
	.lists-container {
		grid-template-columns: 1fr;
	}
	.format-container {
		width: 100%;
	}
}
</style>
</head>
<body>
	<%
	String selectedFormat = null;
	User user = null;
	int userId = (int) request.getAttribute("userId");
	UserDAO userDAO = new UserDAOImpl();
	selectedFormat = userDAO.getFormat(userId);

	if (selectedFormat == null) {
		selectedFormat = "DD-MM-YYYY HH:mm";
	}

	user = UserCache.getInstance().getUser(userId);
	if(user ==  null) {
		user = userDAO.getUserById(userId);
	}
	ContactDAOImpl contactDAO = new ContactDAOImpl();
	List<Contact> contactList = contactDAO.getContactsByUserId(userId);

	GroupDAO groupDAO = new GroupDAOImpl();
	List<Group> groupList = groupDAO.getUserGroupsWithIds(userId);
	%>

	<div class="dashboard-wrapper">
		<header class="header">
			<h1>
				Welcome,
				<%=user.getUsername()%></h1>
			<div class="dashboard-actions">
				<form action="profile.jsp" method="get">
					<button type="submit" class="btn">Profile</button>
				</form>
			</div>
		</header>

		<div class="main-content">
			<div class="lists-container">
				<div class="dashboard-section">
					<div class="section-header">
						<h2>Your Groups</h2>
						<form action="creategroup.jsp" method="get">
							<button type="submit" class="btn">Create New Group</button>
						</form>
					</div>
					<div class="list-section-content">
						<div class="list-items">
							<%
							if (groupList != null && !groupList.isEmpty()) {
								for (Group group : groupList) {
							%>
							<a
								href="ViewGroupServlet?groupId=<%=group.getGroupId()%>"
								class="btn"> <%=group.getGroupName()%>
							</a>
							<%
							}
							} else {
							%>
							<p>No groups found.</p>
							<%
							}
							%>
						</div>
					</div>
				</div>

				<div class="dashboard-section">
					<div class="section-header">
						<h2>Your Contacts</h2>
						<form action="addcontact.jsp" method="get">
							<button type="submit" class="btn">Add Contact</button>
						</form>
					</div>
					<div class="list-section-content">
						<div class="list-items">
							<%
							if (contactList != null && !contactList.isEmpty()) {
								for (Contact contact : contactList) {
							%>
								<form action="ViewContactServlet" method="post">
									<input type="hidden" name="contactId"
										value="<%=contact.getContactId()%>">
									<button type="submit" class="btn"><%=contact.getUsername()%></button>
								</form>
							<%
							}
							} else {
							%>
							<p>No contacts found.</p>
							<%
							}
							%>
						</div>
					</div>
				</div>
			</div>

			<div class="format-container">
				<div class="format-selector">
					<h3>Date-Time Format</h3>
					<form action="dashboard" method="post">
						<input type="hidden" name="action" value="setFormat"> <select
							name="dateFormat">
							<option value="dd-MM-yyyy HH:mm"
								<%=selectedFormat.equals("dd-MM-yyyy HH:mm") ? "selected" : ""%>>
								DD-MM-YYYY HH:MM</option>
							<option value="yyyy-MM-dd HH:mm:ss"
								<%=selectedFormat.equals("yyyy-MM-dd HH:mm:ss") ? "selected" : ""%>>
								YYYY-MM-DD HH:MM:SS</option>
							<option value="MM-yyyy HH"
								<%=selectedFormat.equals("MM-yyyy HH") ? "selected" : ""%>>
								MM-YYYY HH</option>
						</select>
						<button type="submit" class="btn" style="width: 100%;">Set
							Format</button>
					</form>
				</div>

				<div class="sync-contacts-section">
					<a href="synced-accounts" class="btn">Sync Google Contacts</a>

				</div>

				<div class="logout-section">
					<form action="dashboard" method="post">
						<input type="hidden" name="action" value="logout">
						<button type="submit" class="btn">Logout</button>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>