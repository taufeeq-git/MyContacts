<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.taufeeq.web.model.Contact"%>
<%@ page session="false"%>

<%
Contact contact = (Contact) request.getAttribute("contact");
if (contact == null) {
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
<title>Edit Contact</title>
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

.container {
	background: var(--teeny-green-light);
	border-radius: 16px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	padding: 40px;
	width: 100%;
	max-width: 800px;
}

.header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 20px;
	border-radius: 16px;
	margin-bottom: 30px;
	text-align: center;
}

.section {
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
	margin-bottom: 20px;
}

.form-group {
	margin-bottom: 20px;
}

label {
	display: block;
	margin-bottom: 8px;
	font-weight: 500;
	color: var(--text-primary);
}

input[type="text"], input[type="date"], select {
	width: 100%;
	padding: 12px;
	border: 1px solid var(--blue-green-light);
	border-radius: 8px;
	font-size: 16px;
	transition: border-color 0.3s ease;
}

input[type="text"]:focus, input[type="date"]:focus, select:focus {
	outline: none;
	border-color: var(--blue-green-primary);
	box-shadow: 0 0 0 2px rgba(92, 189, 185, 0.2);
}

.checkbox-group {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 15px;
}

.checkbox-group input[type="checkbox"] {
	width: 20px;
	height: 20px;
	cursor: pointer;
}

.buttons {
	display: flex;
	gap: 10px;
	justify-content: flex-end;
	margin-top: 30px;
}

.btn {
	padding: 12px 24px;
	border: none;
	border-radius: 8px;
	cursor: pointer;
	font-size: 16px;
	font-weight: 500;
	transition: all 0.3s ease;
}

.btn-primary {
	background: var(--blue-green-primary);
	color: var(--text-light);
}

.btn-primary:hover {
	background: var(--blue-green-dark);
	transform: translateY(-2px);
}

.btn-secondary {
	background: var(--pink-primary);
	color: var(--text-primary);
}

.btn-secondary:hover {
	background: var(--pink-dark);
	transform: translateY(-2px);
}

.contact-info {
	display: flex;
	gap: 20px;
	margin-top: 20px;
}

.contact-section {
	flex: 1;
}

.error-message {
	color: #ff6b6b;
	margin-top: 5px;
	font-size: 14px;
}

@media ( max-width : 768px) {
	.container {
		padding: 20px;
	}
	.contact-info {
		flex-direction: column;
	}
	.buttons {
		flex-direction: column;
	}
	.btn {
		width: 100%;
	}
}
</style>
</head>
<body>
	<div class="container">
		<div class="header">
			<h1>Edit Contact</h1>
		</div>

		<form action="editContact" method="post" class="section">
			<input type="hidden" name="action" value="updateContact"> <input
				type="hidden" name="contactId" value="<%=contact.getContactId()%>">

			<div class="form-group">
				<label for="username">Name</label> <input type="text" id="username"
					name="username" value="<%=contact.getUsername()%>" required>
			</div>

			<div class="form-group">
				<label for="gender">Gender</label> <select id="gender" name="gender">
					<option value="">Not specified</option>
					<option value="Male"
						<%="Male".equals(contact.getGender()) ? "selected" : ""%>>Male</option>
					<option value="Female"
						<%="Female".equals(contact.getGender()) ? "selected" : ""%>>Female</option>
				</select>
			</div>

			<div class="form-group">
				<label for="birthday">Birthday</label> <input type="date"
					id="birthday" name="birthday"
					value="<%=contact.getBirthday() != null ? contact.getBirthday() : ""%>">
			</div>

			<div class="checkbox-group">
				<input type="checkbox" id="favorite" name="favorite"
					<%=contact.getFavorite() ? "checked" : ""%>> <label
					for="favorite">Mark as Favorite</label>
			</div>

			<div class="checkbox-group">
				<input type="checkbox" id="archive" name="archive"
					<%=contact.getArchive() ? "checked" : ""%>> <label
					for="archive">Archive Contact</label>
			</div>

			<div class="buttons">
				<form action="ViewContactServlet" method="get"
					style="display: inline;">
					<input type="hidden" name="contactId"
						value="<%=contact.getContactId()%>">
					<button type="submit" class="btn btn-secondary">Cancel</button>
				</form>
				<button type="submit" class="btn btn-primary">Save Changes</button>
			</div>

			<div class="contact-info">
				<div class="contact-section">
					<h3>Email Addresses</h3>
					<%
					if (contact.getEmails() != null && !contact.getEmails().isEmpty()) {
					%>
					<%
					for (String email : contact.getEmails()) {
					%>
					<div style="display: flex; gap: 10px; margin-bottom: 10px;">
						<input type="text" value="<%=email%>" readonly>
						<form action="editContact" method="post" style="display: inline;">
							<input type="hidden" name="action" value="deleteEmail"> <input
								type="hidden" name="email" value="<%=email%>"> <input
								type="hidden" name="contactId"
								value="<%=contact.getContactId()%>">
							<button type="submit" class="btn btn-secondary">Delete</button>
						</form>
					</div>
					<%
					}
					%>
					<%
					}
					%>

					<form action="editContact" method="post">
						<input type="hidden" name="action" value="addEmail"> <input
							type="hidden" name="contactId"
							value="<%=contact.getContactId()%>">
						<div style="display: flex; gap: 10px;">
							<input type="email" name="email" placeholder="Add new email"
								required>
							<button type="submit" class="btn btn-secondary">Add</button>
						</div>
					</form>
				</div>

				<div class="contact-section">
					<h3>Phone Numbers</h3>
					<%
					if (contact.getPhoneNumbers() != null && !contact.getPhoneNumbers().isEmpty()) {
					%>
					<%
					for (String phone : contact.getPhoneNumbers()) {
					%>
					<div style="display: flex; gap: 10px; margin-bottom: 10px;">
						<input type="text" value="<%=phone%>" readonly>
						<form action="editContact" method="post" style="display: inline;">
							<input type="hidden" name="action" value="deletePhoneNumber">
							<input type="hidden" name="phoneNumber" value="<%=phone%>">
							<input type="hidden" name="contactId"
								value="<%=contact.getContactId()%>">
							<button type="submit" class="btn btn-secondary">Delete</button>
						</form>
					</div>
					<%
					}
					%>
					<%
					}
					%>

					<form action="editContact" method="post">
						<input type="hidden" name="action" value="addPhoneNumber">
						<input type="hidden" name="contactId"
							value="<%=contact.getContactId()%>">
						<div style="display: flex; gap: 10px;">
							<input type="tel" name="number"
								placeholder="Add new phone number" required>
							<button type="submit" class="btn btn-secondary">Add</button>
						</div>
					</form>
				</div>
			</div>

		</form>
	</div>
</body>
</html>