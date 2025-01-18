<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.taufeeq.web.dao.*"%>
<%@ include file="clearcache.jsp"%>
<%@ page import="com.taufeeq.web.model.*"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page session="false"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page session="false"%>

<%
int userId = (int) request.getAttribute("userId");
UserDAOImpl user1 = new UserDAOImpl();
User user = user1.getUserById(userId);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Profile</title>
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

.profile-container {
	background: var(--teeny-green-light);
	border-radius: 16px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	padding: 40px;
	width: 100%;
	max-width: 1000px;
	height: calc(100vh - 40px);
	display: flex;
	flex-direction: column;
	gap: 20px;
}

.profile-header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 20px;
	border-radius: 16px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	flex-shrink: 0;
}

.profile-header h1 {
	font-size: 1.8rem;
	font-weight: 600;
	color: var(--text-light);
}

.profile-section {
	display: flex;
	flex-direction: column;
	gap: 20px;
	flex: 1;
	overflow: hidden;
}

.main-form {
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
}

.form-group {
	margin-bottom: 20px;
}

.form-group label {
	display: block;
	margin-bottom: 8px;
	color: var(--text-primary);
	font-weight: 500;
}

.form-group input {
	width: 100%;
	padding: 10px;
	border: 1px solid var(--blue-green-light);
	border-radius: 8px;
	font-size: 1rem;
}

.contact-sections {
	display: flex;
	gap: 20px;
	flex: 1;
	min-height: 0;
}

.contact-section {
	flex: 1;
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
	display: flex;
	flex-direction: column;
}

.contact-section h2 {
	color: var(--blue-green-primary);
	margin-bottom: 15px;
	padding-bottom: 10px;
	border-bottom: 2px solid var(--blue-green-light);
}

.contact-list {
	list-style: none;
	overflow-y: auto;
	flex: 1;
	padding-right: 10px;
}

.contact-item {
	background: var(--text-light);
	padding: 10px 15px;
	border-radius: 8px;
	margin-bottom: 10px;
	display: flex;
	justify-content: space-between;
	align-items: center;
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
}

.btn:hover {
	background: var(--pink-light);
	transform: translateY(-2px);
}

.btn-delete {
	background: #ff6b6b;
	color: white;
	padding: 6px 12px;
	font-size: 0.9rem;
}

.btn-delete:hover {
	background: #ff5252;
}

.add-form {
	display: flex;
	gap: 10px;
	margin-top: 15px;
	margin-bottom: 15px;
}

.add-form input {
	flex: 1;
	padding: 10px;
	border: 1px solid var(--blue-green-light);
	border-radius: 8px;
}

.back-btn {
	color: var(--text-light);
	text-decoration: none;
	padding: 10px 20px;
	border-radius: 12px;
	background: var(--blue-green-dark);
	transition: all 0.3s ease;
}

.back-btn:hover {
	background: var(--blue-green-primary);
	transform: translateY(-2px);
}

/* Custom scrollbar styles */
.contact-list::-webkit-scrollbar {
	width: 8px;
}

.contact-list::-webkit-scrollbar-track {
	background: var(--pink-light);
	border-radius: 4px;
}

.contact-list::-webkit-scrollbar-thumb {
	background: var(--blue-green-light);
	border-radius: 4px;
}

.contact-list::-webkit-scrollbar-thumb:hover {
	background: var(--blue-green-primary);
}

@media ( max-width : 768px) {
	.contact-sections {
		flex-direction: column;
	}
	.profile-container {
		padding: 20px;
		height: auto;
	}
	.profile-header {
		flex-direction: column;
		text-align: center;
		gap: 15px;
	}
}
</style>
</head>
<body>
	<div class="profile-container">
		<div class="profile-header">
			<h1>Edit Your Profile</h1>
			<a href="profile.jsp" class="back-btn">Back to Profile</a>
		</div>

		<div class="profile-section">
			<form action="editProfile?action=editProfile" method="post"
				class="main-form">
				<input type="hidden" name="userId" value="<%=user.getUserId()%>">

				<div class="form-group">
					<label for="username">Username:</label> <input type="text"
						id="username" name="username" value="<%=user.getUsername()%>"
						required>
				</div>

				<div class="form-group">
					<label for="gender">Gender:</label> <input type="text" id="gender"
						name="gender" value="<%=user.getGender()%>" required>
				</div>

				<div class="form-group">
					<label for="birthday">Birthday:</label> <input type="date"
						id="birthday" name="birthday"
						value="<%=user.getBirthday() != null ? new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()) : ""%>">
				</div>

				<div class="form-group">
					<label for="location">Location:</label> <input type="text"
						id="location" name="location" value="<%=user.getLocation()%>">
				</div>

				<button type="submit" class="btn">Save Changes</button>
			</form>

			<div class="contact-sections">
				<div class="contact-section">
					<h2>Emails</h2>
					<form action="editProfile?action=addEmail" method="post"
						class="add-form">
						<input type="email" name="email"
							placeholder="Enter alternate email" required>
						<button type="submit" class="btn">Add Email</button>
					</form>
					<ul class="contact-list">
						<%
						List<Email> emails = user.getEmails();
						if (emails != null && !emails.isEmpty()) {
							for (Email email : emails) {
						%>
						<li class="contact-item"><%=email.getEmail()%>
							<form action="editProfile?action=deleteEmail" method="post"
								style="display: inline;">
								<input type="hidden" name="userId"
									value="<%=user.getUserId()%>"> <input type="hidden"
									name="email" value="<%=email.getEmail()%>">
								<button type="submit" class="btn btn-delete">Delete</button>
							</form></li>
						<%
						}
						} else {
						%>
						<li class="contact-item">No emails available</li>
						<%
						}
						%>
					</ul>
				</div>

				<div class="contact-section">
					<h2>Phone Numbers</h2>
					<form action="editProfile?action=addPhoneNumber" method="post"
						class="add-form">
						<input type="tel" name="number" placeholder="Enter phone number"
							required>
						<button type="submit" class="btn">Add Phone Number</button>
					</form>
					<ul class="contact-list">
						<%
						List<PhoneNumber> phoneNumbers = user.getPhoneNumbers();
						if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
							for (PhoneNumber phoneNumber : phoneNumbers) {
								if (phoneNumber != null && phoneNumber.getPhoneNumber() != null) {
						%>
						<li class="contact-item"><%=phoneNumber.getPhoneNumber()%>
							<form action="editProfile?action=deletePhoneNumber" method="post"
								style="display: inline;">
								<input type="hidden" name="userId"
									value="<%=user.getUserId()%>"> <input type="hidden"
									name="phoneNumber" value="<%=phoneNumber.getPhoneNumber()%>">
								<button type="submit" class="btn btn-delete">Delete</button>
							</form></li>
						<%
						}
						}
						} else {
						%>
						<li class="contact-item">No phone numbers available</li>
						<%
						}
						%>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>