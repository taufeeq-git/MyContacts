<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="clearcache.jsp" %>
<%@ page session="false" %> 
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Contact</title>
    
</head>
<body>
    <div class="container">
        <h1>New Contact</h1>
        <form action="addcontact" method="post">
            <input type="text" name="username" placeholder="Username" required><br><br>
            <input type="email" name="email" placeholder="Email" required><br><br>
            <input type="text" name="number" placeholder="Phone number" pattern="[0-9]{10}" required><br><br>
            <input type="text" name="gender" placeholder="Gender" required><br><br>
            <input type="date" name="dob" placeholder="YYYY-MM-DD(DOB)" required><br><br>
  			<label for="favorite">Favorite:</label>
       	    <input type="checkbox" id="favorite" name="favorite" value="1"><br/>
       	    <label for="archive">Archive:</label>
        	<input type="checkbox" id="archive" name="archive" value="1"><br/>
            <input type="submit" value="Submit">
        </form>
    </div>
    </form> <br><br>
        <form action="dashboard" method="get">
        <button type="submit">Back to Dashboard</button>
    </form>
</body>
</html>
