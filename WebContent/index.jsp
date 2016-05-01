<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login Application</title>
</head>
<body>
	<form action="loginServlet" method="post">
			<legend> Login to App </legend>
					User ID<input type="text" name="username" required="required" />
					Password<input type="password" name="userpass" required="required" />
					<input type="submit" value="Login" />
				
	</form>
	<form action = "register.jsp">
    <input type="submit" value="New Users must register here" />
	</form>
</body>
</html>