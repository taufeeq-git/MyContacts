<!-- ViewGroup.jsp -->
<%@ page import="java.util.List"%>
<%@ include file="clearcache.jsp"%>
<%@ page import="com.taufeeq.web.model.Contact"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Group</title>
<style>
:root {
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
	display: flex;
	justify-content: center;
	padding: 20px;
}

.container {
	background: var(--teeny-green-light);
	border-radius: 16px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	padding: 40px;
	width: 100%;
	max-width: 1000px;
	min-height: calc(100vh - 40px);
	display: flex;
	flex-direction: column;
}

.header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 20px;
	border-radius: 16px;
	margin-bottom: 30px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.section {
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
	margin-bottom: 20px;
}

.info-item {
	background: var(--text-light);
	padding: 15px;
	border-radius: 8px;
	margin-bottom: 10px;
}

.contact-sections {
	display: flex;
	gap: 20px;
	margin-top: 20px;
}

.contact-section {
	flex: 1;
}

.btn {
	background: var(--pink-primary);
	color: var(--text-primary);
	border: none;
	padding: 10px 20px;
	border-radius: 12px;
	cursor: pointer;
	transition: all 0.3s ease;
	text-decoration: none;
	display: inline-block;
	margin: 5px;
}

.btn:hover {
	background: var(--pink-light);
	transform: translateY(-2px);
}

.btn-danger {
	background: #ff6b6b;
	color: white;
}

.btn-danger:hover {
	background: #ff5252;
}

input[type="email"], input[type="tel"], select {
	width: 100%;
	padding: 10px;
	border: 1px solid var(--blue-green-light);
	border-radius: 8px;
	margin-bottom: 10px;
}

h2 {
	color: var(--blue-green-primary);
	margin-bottom: 15px;
	border-bottom: 2px solid var(--blue-green-light);
	padding-bottom: 10px;
}

ul {
	list-style: none;
	padding: 0;
}

li {
	background: var(--text-light);
	padding: 10px 15px;
	border-radius: 8px;
	margin-bottom: 10px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

@media ( max-width : 768px) {
	.container {
		padding: 20px;
	}
	.contact-sections {
		flex-direction: column;
	}
	.header {
		flex-direction: column;
		text-align: center;
	}
	.btn {
		width: 100%;
		margin: 5px 0;
	}
}
</style>
</head>
<body>
	<div class="container">
		<div class="header">
			<h1>
				Contacts of group:
				<%=request.getAttribute("groupName")%></h1>
			<form action="dashboard" method="get">
				<button type="submit" class="btn">Back to Dashboard</button>
			</form>
		</div>

		<div class="section">
			<h2>Group Members</h2>
			<ul>
				<%
				List<Contact> groupContacts = (List<Contact>) request.getAttribute("groupContacts");
				if (groupContacts != null && !groupContacts.isEmpty()) {
					for (Contact contact : groupContacts) {
				%>
				<li><span><%=contact.getUsername()%></span>
					<div>
						<form action="ViewContactServlet" method="post"
							style="display: inline;">
							<input type="hidden" name="contactId"
								value="<%=contact.getContactId()%>">
							<button type="submit" class="btn">View</button>
						</form>
						<form action="AddContactToGroupServlet" method="post"
							style="display: inline;">
							<input type="hidden" name="contactId"
								value="<%=contact.getContactId()%>"> <input
								type="hidden" name="groupId"
								value="<%=request.getAttribute("groupId")%>"> <input
								type="hidden" name="action" value="delete">
							<button type="submit" class="btn btn-danger">Remove</button>
						</form>
					</div></li>
				<%
				}
				} else {
				%>
				<li>No contacts in this group.</li>
				<%
				}
				%>
			</ul>
		</div>

		<div class="section">
			<h2>Add a Contact to this Group</h2>
			<form action="AddContactToGroupServlet" method="post">
				<input type="hidden" name="groupId"
					value="<%=request.getAttribute("groupId")%>"> <input
					type="hidden" name="action" value="add"> <select
					name="contactId" required>
					<option value="">-- Select a contact --</option>
					<%
					List<Contact> userContacts = (List<Contact>) request.getAttribute("userContacts");
					if (userContacts != null && !userContacts.isEmpty()) {
						for (Contact contact : userContacts) {
					%>
					<option value="<%=contact.getContactId()%>"><%=contact.getUsername()%></option>
					<%
					}
					} 
					%>
				</select>
				<button type="submit" class="btn">Add Contact</button>
			</form>
		</div>
	</div>
</body>
</html>