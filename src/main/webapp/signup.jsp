<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="clearcache.jsp"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>MyContacts - Sign Up</title>
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
	padding: 40px 20px;
}

.container {
	background: var(--teeny-green-light);
	padding: 40px;
	border-radius: 16px;
	box-shadow: 0 8px 16px var(--shadow-subtle);
	width: 100%;
	max-width: 500px;
}

h1 {
	color: var(--blue-green-primary);
	margin-bottom: 30px;
	font-size: 2rem;
	font-weight: 600;
	text-align: center;
}

form {
	display: flex;
	flex-direction: column;
	gap: 20px;
}

input, select {
	padding: 12px;
	border: 2px solid var(--teeny-green-primary);
	border-radius: 10px;
	font-size: 1rem;
	transition: all 0.3s ease;
	background-color: white;
}

input:focus, select:focus {
	outline: none;
	border-color: var(--blue-green-primary);
	box-shadow: 0 0 0 3px var(--shadow-subtle);
}

select {
	cursor: pointer;
	appearance: none;
	background-image:
		url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
	background-repeat: no-repeat;
	background-position: right 1rem center;
	background-size: 1em;
}

input[type="submit"] {
	background: var(--blue-green-primary);
	color: var(--text-light);
	border: none;
	padding: 14px;
	font-weight: 600;
	cursor: pointer;
	transition: all 0.3s ease;
	margin-top: 10px;
}

input[type="submit"]:hover {
	background: var(--blue-green-light);
	transform: translateY(-2px);
}

.login-link {
	margin-top: 20px;
	text-align: center;
}

a {
	color: var(--blue-green-primary);
	text-decoration: none;
	font-weight: 600;
	transition: color 0.3s ease;
}

a:hover {
	color: var(--blue-green-dark);
}

input[type="date"] {
	appearance: none;
	-webkit-appearance: none;
	color: var(--text-primary);
	font-family: 'Inter', sans-serif;
	font-size: 1rem;
}

.form-group {
	display: flex;
	flex-direction: column;
	gap: 8px;
	margin-bottom: 15px; 
}

.form-group label {
	font-weight: 500;
	color: var(--text-secondary);
}

.required-star {
	color: red;
	margin-left: 2px;
}

br {
	display: none;
}
</style>
</head>
<body>
	<div class="container">
		<h1>Sign Up for MyContacts</h1>
		<form action="signup" method="post">
			<div class="form-group">
				<label for="username">Username<span class="required-star">*</span></label>
				<input type="text" id="username" name="username"
					placeholder="Username" required>
			</div>
			<div class="form-group">
				<label for="email">Email<span class="required-star">*</span></label>
				<input type="email" id="email" name="email" placeholder="Email"
					required>
			</div>
			<div class="form-group">
				<label for="password">Password<span class="required-star">*</span></label>
				<input type="password" id="password" name="password"
					placeholder="Password" required>
			</div>
			<div class="form-group">
				<label for="number">Phone number<span class="required-star">*</span>
				</label> <input type="text" id="number" name="number"
					placeholder="Phone number (10 digits)" pattern="[0-9]{10}" required>
			</div>
			<div class="form-group">
				<label for="gender">Gender<span class="required-star">*</span></label>
				<select id="gender" name="gender" required>
					<option value="" disabled selected>Select gender</option>
					<option value="Male">Male</option>
					<option value="Female">Female</option>
					<option value="Other">Other</option>
				</select>
			</div>
			<div class="form-group">
				<label for="dob">Date of Birth</label> <input type="date" name="dob"
					id="dob">
			</div>
			<div class="form-group">
				<label for="location">Location</label> <input type="text"
					id="location" name="location" placeholder="Location">
			</div>
			<input type="submit" value="Sign Up">
		</form>
		<div class="login-link">
			<p>
				Already have an account? <a href="login.jsp">Login</a>
			</p>
		</div>
	</div>
</body>
</html>