<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Post a picture</title>
</head>
<body>
	<form action="PostServlet" method="post" enctype="multipart/form-data">

		<table>
			<tr>
				<td>Please specify an image:<br></td>
				<td><input type="file" name="datafile"></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><input type="text" name="description" required="required" /></td>
			</tr>
			<tr>
				<td>Price</td>
				<td><input type="text" name="price" required="required" /></td>
			</tr>
			<tr>
				<td>Select Category <select name="category">
						<option value="Laptop">Laptop</option>
						<option value="Mobile">Mobile Phones</option>
				</select>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="submit" /></td>
			</tr>
			
		</table>
	</form>
</body>
</html>
