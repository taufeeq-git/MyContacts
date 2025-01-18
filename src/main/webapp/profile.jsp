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
<%@ page import="com.taufeeq.web.cache.*"%>

<%
int userId = (int) request.getAttribute("userId");
User user = UserCache.getInstance().getUser(userId);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>User Profile</title>
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
}

.profile-header {
	background: var(--blue-green-primary);
	color: var(--text-light);
	padding: 20px;
	border-radius: 16px;
	margin-bottom: 30px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	flex-shrink: 0;
}

.profile-header h1 {
	font-size: 1.8rem;
	font-weight: 600;
}

.profile-section {
	background: var(--pink-light);
	border-radius: 12px;
	padding: 20px;
	margin-bottom: 20px;
	flex: 1;
	min-height: 0;
	display: flex;
	flex-direction: column;
}

.personal-info {
	margin-bottom: 20px;
	flex-shrink: 0;
}

.contact-sections {
	display: flex;
	gap: 20px;
	flex: 1;
	min-height: 0;
}

.contact-section {
	flex: 1;
	display: flex;
	flex-direction: column;
	min-height: 0;
}

.add-contact-form {
	background: var(--pink-light);
	padding: 15px;
	margin-bottom: 15px;
	border-radius: 8px;
	flex-shrink: 0;
}

.add-contact-form input {
	flex-grow: 1;
	padding: 10px;
	border-radius: 8px;
	border: 1px solid var(--blue-green-light);
}

.scrollable-content {
	overflow-y: auto;
	flex: 1;
	min-height: 0;
}

.profile-details {
	display: flex;
	flex-direction: column;
	gap: 10px;
	padding-right: 10px;
}

.info-item {
	background: var(--text-light);
	padding: 10px 15px;
	border-radius: 8px;
	display: flex;
	flex-direction: column;
}

.info-item-main {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.info-item-time {
	font-size: 0.8rem;
	color: var(--text-secondary);
	margin-top: 5px;
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

.actions {
	display: flex;
	gap: 10px;
}

/* Scrollbar styling */
.scrollable-content::-webkit-scrollbar {
	width: 8px;
}

.scrollable-content::-webkit-scrollbar-track {
	background: var(--teeny-green-light);
	border-radius: 10px;
}

.scrollable-content::-webkit-scrollbar-thumb {
	background: var(--blue-green-light);
	border-radius: 10px;
}

.scrollable-content::-webkit-scrollbar-thumb:hover {
	background: var(--blue-green-primary);
}

/* For Firefox */
.scrollable-content {
	scrollbar-width: thin;
	scrollbar-color: var(--blue-green-light) var(--teeny-green-light);
}

@media ( max-width : 768px) {
	.contact-sections {
		flex-direction: column;
	}
	.profile-container {
		padding: 20px;
		height: auto;
		min-height: 100vh;
	}
	.profile-header {
		flex-direction: column;
		text-align: center;
	}
	.actions {
		flex-direction: column;
	}
	.contact-section {
		min-height: 300px;
	}
}
</style>
</head>
<body>
	<div class="profile-container">
		<div class="profile-header">
			<h1>Your Profile</h1>
			<div class="actions">
				<a href="editProfile.jsp" class="btn">Edit Profile</a> <a
					href="dashboard" class="btn">Back to Dashboard</a>
			</div>
		</div>

		<div class="personal-info">
			<h2
				style="color: var(--blue-green-primary); margin-bottom: 15px; border-bottom: 2px solid var(--blue-green-light); padding-bottom: 10px;">
				Personal Information</h2>
			<div class="profile-details">
				<div class="info-item">
					<div class="info-item-main">
						<strong>Username:</strong>
						<%=user.getUsername()%>
					</div>
				</div>
				<div class="info-item">
					<div class="info-item-main">
						<strong>Gender:</strong>
						<%=user.getGender()%>
					</div>
				</div>
				<div class="info-item">
					<div class="info-item-main">
						<strong>Birthday:</strong>
						<%=user.getBirthday() != null ? new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()) : "Not available"%>
					</div>
				</div>
				<div class="info-item">
					<div class="info-item-main">
						<strong>Location:</strong>
						<%=user.getLocation()%>
					</div>
				</div>
			</div>
		</div>

		<div class="profile-section">
			<h2
				style="color: var(--blue-green-primary); margin-bottom: 15px; border-bottom: 2px solid var(--blue-green-light); padding-bottom: 10px;">
				Contact Information</h2>
			<div class="contact-sections">
				<div class="contact-section">
					<h3 style="color: var(--blue-green-primary); margin-bottom: 10px;">Emails</h3>
					<div class="add-contact-form">
						<form action="dashboard?action=addEmail" method="post">
							<div style="display: flex; gap: 10px;">
								<input type="email" name="email"
									placeholder="Enter alternate email" required>
								<button type="submit" class="btn">Add Email</button>
							</div>
						</form>
					</div>
					<div class="scrollable-content">
						<div class="profile-details">
							<%
							List<Email> emails = user.getEmails();
							if (emails != null && !emails.isEmpty()) {
								for (Email email : emails) {
									long createdTimeEpoch = email.getCreatedTime();

									SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
									String formattedDate = formatter.format(new Date(createdTimeEpoch * 1000));
							%>
							<div class="info-item">
								<div class="info-item-main">
									<%=email.getEmail()%>
								</div>
								<div class="info-item-time">
									Added:
									<%=formattedDate%>
								</div>
							</div>
							<%
							}
							} else {
							%>
							<div class="info-item">No emails available</div>
							<%
							}
							%>
						</div>
					</div>
				</div>

				<div class="contact-section">
					<h3 style="color: var(--blue-green-primary); margin-bottom: 10px;">Phone
						Numbers</h3>
					<div class="add-contact-form">
						<form action="dashboard?action=addPhoneNumber" method="post">
							<div style="display: flex; gap: 10px;">
								<input type="tel" name="number" placeholder="Enter phone number"
									required>
								<button type="submit" class="btn">Add Phone Number</button>
							</div>
						</form>
					</div>
					<div class="scrollable-content">
						<div class="profile-details">
							<%
							List<PhoneNumber> phoneNumbers = user.getPhoneNumbers();
							if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
								for (PhoneNumber phoneNumber : phoneNumbers) {
									if (phoneNumber != null && phoneNumber.getPhoneNumber() != null) {
								long createdTimeEpoch = phoneNumber.getCreatedTime();
								SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
								String formattedDate = formatter.format(new Date(createdTimeEpoch * 1000));
							%>
							<div class="info-item">
								<div class="info-item-main">
									<%=phoneNumber.getPhoneNumber()%>
								</div>
								<div class="info-item-time">
									Added:
									<%=formattedDate%>
								</div>
							</div>
							<%
							}
							}
							} else {
							%>
							<div class="info-item">No phone numbers available</div>
							<%
							}
							%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>