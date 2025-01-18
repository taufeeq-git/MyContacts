<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.taufeeq.web.model.Contact"%>
<%@ include file="clearcache.jsp"%>
<%@ page session="false"%>

<%
Contact contact = (Contact) request.getAttribute("contact");
int contactId = (int) request.getAttribute("contactId");
if (contact == null) {
	response.sendRedirect("dashboard?error=ContactNotFound");
	return;
}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Contact Details</title>
<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap"
	rel="stylesheet">
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

.contact-container {
	background: var(--teeny-green-light);
	border-radius: 16px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	padding: 40px;
	width: 100%;
	max-width: 1000px;
	height: calc(100vh - 40px);
	display: flex;
	flex-direction: column;
}

.contact-header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 20px;
	border-radius: 16px;
	margin-bottom: 30px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.contact-section {
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
	margin-bottom: 20px;
	flex: 1;
	overflow-y: auto;
}

.info-group {
	background: var(--text-light);
	padding: 15px;
	border-radius: 8px;
	margin-bottom: 15px;
}

.contact-details {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
	gap: 20px;
	margin-bottom: 30px;
}

.btn {
	background: var(--pink-primary);
	color: var(--text-primary);
	border: none;
	padding: 10px 20px;
	border-radius: 12px;
	cursor: pointer;
	transition: all 0.3s ease;
	font-size: 14px;
}

.btn:hover {
	background: var(--pink-dark);
	transform: translateY(-2px);
}

.btn-danger {
	background: #ff6b6b;
	color: white;
}

.btn-danger:hover {
	background: #ff5252;
}

.form-group {
	margin-bottom: 15px;
}

input[type="email"], input[type="tel"] {
	width: 100%;
	padding: 10px;
	border: 1px solid var(--blue-green-light);
	border-radius: 8px;
	margin-bottom: 10px;
}

ul {
	list-style: none;
	padding: 0;
}

li {
	background: white;
	padding: 10px;
	margin-bottom: 8px;
	border-radius: 6px;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.section-title {
	color: var(--blue-green-primary);
	margin-bottom: 15px;
	border-bottom: 2px solid var(--blue-green-light);
	padding-bottom: 10px;
}

@media ( max-width : 768px) {
	.contact-container {
		padding: 20px;
		height: auto;
	}
	.contact-header {
		flex-direction: column;
		text-align: center;
		gap: 15px;
	}
	.contact-details {
		grid-template-columns: 1fr;
	}
}
</style>
</head>
<body>
	<div class="contact-container">
		<div class="contact-header">
			<h1>
				Contact Details for
				<%=contact.getUsername()%></h1>
			<div>
				<form action="editContact" method="get"
					style="display: inline-block;">
					<input type="hidden" name="contactId" value="${contactId}" />
					<button type="submit" class="btn">Edit Contact</button>
				</form>
				<form action="dashboard" method="get"
					style="display: inline-block; margin-left: 10px;">
					<button type="submit" class="btn">Back to Dashboard</button>
				</form>
			</div>
		</div>

		<div class="contact-section">
			<div class="info-group">
				<h2 class="section-title">General Information</h2>
				<div class="contact-details">
					<div>
						<p>
							<strong>Name:</strong>
							<%=contact.getUsername()%></p>
						<p>
							<strong>Gender:</strong>
							<%=contact.getGender() != null ? contact.getGender() : "Not specified"%></p>
						<p>
							<strong>Birthday:</strong>
							<%=contact.getBirthday() != null ? contact.getBirthday() : "Not specified"%></p>
					</div>
					<div>
						<p>
							<strong>Favorite:</strong>
							<%=contact.getFavorite() == false ? "No" : "Yes"%></p>
						<p>
							<strong>Archived:</strong>
							<%=contact.getArchive() == false ? "No" : "Yes"%></p>
						<p>
							<strong>Created:</strong>
							<%=contact.getFormattedCreatedTime() != null ? contact.getFormattedCreatedTime() : "Not available"%></p>
					</div>
				</div>
			</div>

			<div class="info-group">
				<h2 class="section-title">Emails</h2>
				<ul>
					<%
					if (contact.getEmails() != null && !contact.getEmails().isEmpty()) {
						for (String email : contact.getEmails()) {
					%>
					<li><%=email%></li>
					<%
					}
					} else {
					%>
					<li>No email addresses available</li>
					<%
					}
					%>
				</ul>
				<form action="ViewContactServlet" method="post" class="form-group">
					<input type="hidden" name="contactId" value="${contactId}" /> <input
						type="hidden" name="action" value="addEmail" /> <input
						type="email" name="newEmail" placeholder="Enter alternate email"
						required />
					<button type="submit" class="btn">Add Email</button>
				</form>
			</div>

			<div class="info-group">
				<h2 class="section-title">Phone Numbers</h2>
				<ul>
					<%
					if (contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty()) {
						for (String phone : contact.getPhoneNumbers()) {
					%>
					<li><%=phone%></li>
					<%
					}
					} else {
					%>
					<li>No phone numbers available</li>
					<%
					}
					%>
				</ul>
				<form action="ViewContactServlet" method="post" class="form-group">
					<input type="hidden" name="contactId" value="${contactId}" /> <input
						type="hidden" name="action" value="addPhoneNumber" /> <input
						type="tel" name="newPhoneNumber"
						placeholder="Enter alternate phone number" required />
					<button type="submit" class="btn">Add Phone Number</button>
				</form>
			</div>

			<div class="info-group">
				<h2 class="section-title">Delete Contact</h2>
				<form action="ViewContactServlet" method="post">
					<input type="hidden" name="contactId" value="${contactId}" /> <input
						type="hidden" name="action" value="deleteContact" />
					<button type="submit" class="btn btn-danger"
						onclick="return confirm('Are you sure you want to delete this contact?');">
						Delete Contact</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>