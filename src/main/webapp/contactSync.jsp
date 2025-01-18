<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.taufeeq.web.model.*"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Synced Accounts</title>
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
	align-items: center;
}

.container {
	background: var(--teeny-green-light);
	border-radius: 16px;
	padding: 40px;
	width: 100%;
	max-width: 700px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
}

h1, h2 {
	color: var(--blue-green-primary);
	margin-bottom: 30px;
	text-align: center;
	font-weight: 600;
}

.sync-message {
	text-align: center;
	margin-bottom: 20px;
	color: green;
	font-weight: 500;
}

.account-item {
	display: flex;
	align-items: center;
	gap: 15px;
	padding: 15px;
	background: var(--text-light);
	border: 2px solid var(--blue-green-light);
	border-radius: 8px;
	margin-bottom: 15px;
	width: 100%;
}

.account-details {
	flex: 1;
	min-width: 0;
}

.account-details span {
	display: inline;
	overflow: visible;
	text-overflow: initial;
}

.action-buttons {
	display: flex;
	gap: 10px;
	align-items: center;
	flex-wrap: nowrap;
	margin-left: 15px;
}


.sync-interval-form {
	display: flex;
	align-items: center;
	gap: 10px;
	flex-shrink: 0;
}
.sync-label{
	font-size:13px
}

input[type="number"] {
	width: 80px;
	padding: 8px;
	border: 2px solid var(--blue-green-light);
	border-radius: 8px;
	font-size: 0.9rem;
	flex-shrink: 0;
}

button, input[type="submit"] {
	background: var(--blue-green-primary);
	color: var(--text-light);
	border: none;
	padding: 10px 15px;
	border-radius: 8px;
	cursor: pointer;
	font-weight: 500;
	transition: all 0.3s ease;
	height: 38px;
	display: flex;
	align-items: center;
	justify-content: center;
	white-space: nowrap;
}

button:hover, input[type="submit"]:hover {
	background: var(--blue-green-light);
	transform: translateY(-2px);
}

.back-button {
	background: var(--pink-primary);
	color: var(--text-primary);
	margin-top: 20px;
	width: 100%;
}

.back-button:hover {
	background: var(--pink-light);
}

.no-accounts {
	text-align: center;
	color: var(--text-secondary);
	margin-bottom: 20px;
}

.google-icon {
	width: 24px;
	height: 24px;
	border-radius: 50%;
	flex-shrink: 0;
}

form {
	margin: 0;
}
</style>
</head>
<body>
	<div class="container">
		<h1>Synced Accounts</h1>

		<%-- Display Sync Success Message --%>
		<%
		Boolean syncSuccess = (Boolean) request.getAttribute("syncSuccess");
		Integer totalContacts = (Integer) request.getAttribute("totalContacts");
		Integer newContacts = (Integer) request.getAttribute("newContacts");

		if (syncSuccess != null && syncSuccess) {
		%>
		<p class="sync-message">
			Sync Successful! Total:
			<%=totalContacts%>, New:
			<%=newContacts%></p>
		<%
		}
		%>

		<%-- Display Synced Accounts --%>
		<%
		List<Token> accounts = (List<Token>) request.getAttribute("accounts");
		if (accounts != null && !accounts.isEmpty()) {
			for (Token account : accounts) {
		%>
		<div class="account-item">
			<%
			if ("GOOGLE".equalsIgnoreCase(account.getProvider())) {
			%>
			<img src="https://www.google.com/favicon.ico" alt="Google"
				class="google-icon">
			<%
			}
			%>
			<div class="account-details">
				<span><%=account.getEmail()%></span>
			</div>
			<div class="action-buttons">
				<form class="sync-interval-form" action="synced-accounts"
					method="POST">
					<input type="hidden" name="action" value="update"> <input
						type="hidden" name="accountId" value="<%=account.getId()%>">
						<div class="sync-container" >
							<div class="sync-label">Sync Interval(hrs)</div>
							<input type="number" name="syncInterval" min="1"
							value="<%=account.getSyncInterval()%>"placeholder="Hours">	
						</div>
					
					<button type="submit">Update</button>
				</form>
				<form class="sync-interval-form" action="synced-accounts"
					method="POST">
					<input type="hidden" name="action" value="syncNow"> <input
						type="hidden" name="accountId" value="<%=account.getId()%>">
					<button type="submit">Sync Now</button>
				</form>
			</div>
		</div>
		<%
		}
		} else {
		%>
		<p class="no-accounts">No synced accounts found.</p>
		<%
		}
		%>

		<form action="google-contact-auth" method="GET">
			<button type="submit">Sync Another Account</button>
		</form>

		<form action="dashboard" method="get">
			<button type="submit" class="back-button">Back to Dashboard</button>
		</form>
	</div>
</body>
</html>