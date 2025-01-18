<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession"%>
<%@ page import="java.io.IOException"%>
<%@ page import="com.taufeeq.web.dao.GroupDAO"%>
<%@ page import="com.taufeeq.web.dao.GroupDAOImpl"%>
<%@ page session="false"%>
<%@ page import="com.taufeeq.web.model.Group"%>
<%@ include file="clearcache.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Create New Group</title>
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
	max-width: 500px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
}

h1 {
	color: var(--blue-green-primary);
	margin-bottom: 30px;
	text-align: center;
	font-weight: 600;
}

form {
	display: flex;
	flex-direction: column;
	gap: 20px;
}

input[type="text"], input[type="email"], input[type="date"] {
	width: 100%;
	padding: 12px;
	border: 2px solid var(--blue-green-light);
	border-radius: 8px;
	font-size: 1rem;
	transition: border-color 0.3s ease;
}

input[type="text"]:focus, input[type="email"]:focus, input[type="date"]:focus
	{
	outline: none;
	border-color: var(--blue-green-primary);
}

.checkbox-group {
	display: flex;
	align-items: center;
	gap: 10px;
}

label {
	font-weight: 500;
	color: var(--text-secondary);
}

button, input[type="submit"] {
	background: var(--blue-green-primary);
	color: var(--text-light);
	border: none;
	padding: 12px 24px;
	border-radius: 8px;
	cursor: pointer;
	font-weight: 500;
	transition: all 0.3s ease;
	width: 100%;
}

button:hover, input[type="submit"]:hover {
	background: var(--blue-green-light);
	transform: translateY(-2px);
}

.back-button {
	background: var(--pink-primary);
	color: var(--text-primary);
	margin-top: 20px;
}

.back-button:hover {
	background: var(--pink-light);
}
</style>
</head>
<body>
	<div class="container">
		<h1>Create New Group</h1>
		<form action="CreateGroupServlet" method="post">
			<input type="text" id="groupName" name="groupName"
				placeholder="Enter group name" required> <input
				type="hidden" name="userId"
				value="<%=request.getAttribute("userId")%>">
			<button type="submit">Create Group</button>
		</form>
		<form action="dashboard.jsp" method="get">
			<button type="submit" class="back-button">Back to Dashboard</button>
		</form>
	</div>
</body>
</html>