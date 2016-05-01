<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action ="RegisterServlet" method= "post" >
Full Name* : <input type="text"  name="name"><br><br>
Email*     : <input type="text"  name="email"><br> <br>
Username*  : <input type="text"  name="user"><br> <br>
Password*  : <input type="password"  name="pass"><br> <br>
<input type="submit" value="register" name="register" >
<input type="submit" value="Login Now" name="reglogin" >
<%if(session.getAttribute("msg")!=null){
	out.println(session.getAttribute("msg"));
	session.setAttribute("msg",null);
}
	%>
</form>
</body>
</html>