<%@page import="com.amazonaws.services.dynamodbv2.document.DynamoDB"%>
<%@page import="com.amzi.dao.AWSDynamodb"%>
<%@page import="com.amazonaws.services.dynamodbv2.model.ScanResult"%>
<%@page import="com.amazonaws.services.dynamodbv2.model.AttributeValue"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.File"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome <%=session.getAttribute("name")%></title>
</head>
<body>
	<h3>Login successful!!!</h3>
	<form action="welcome.jsp" method="GET">
		Select Category <select name="category">
			<option value="All">All</option>
			<option value="Laptop">Laptop</option>
			<option value="Mobile">Mobile Phones</option>
		</select> <input type="submit" value="display items">
	</form>
	<form action="board.jsp" method="GET">
		<input type="submit" value="post new items">
	</form>

	<table border="1">
		<tr>
			<th>Posted By</th>
			<th>Image</th>
			<th>Description</th>
			<th>Price</th>
			<th>Category</th>
			<th>Delete your posted Items</th>

		</tr>

		<%
			AWSDynamodb dynamodb = new AWSDynamodb();

			ScanResult result = dynamodb.dynamo("pictures");
			for (Map<String, AttributeValue> item : result.getItems()) {
				if ("GET".equalsIgnoreCase(request.getMethod())
						&& (request.getParameter("category").equals(
								item.get("category").getS()) || request
								.getParameter("category").equals("All"))) {
		%><tr>
			<td>
				<%
				if(request.getAttribute("name")!=null)
					out.print("<P>" + item.get("username").getS() + "</p>");
				%>
			</td>
			<%
				String imageSource = "https://s3-us-west-2.amazonaws.com/s3cart/"
								+ item.get("image").getS();
			%><td>
				<%
					out.print("<img border='0' src=" + imageSource
									+ " width='150' height='100'");
				%>
			</td>
			<%
				
			%><td>
				<%
					out.print("<P>" + item.get("price").getS() + "</p>");
				%>
			</td>
			<%
				
			%><td>
				<%
					out.print("<P>" + item.get("desc").getS() + "</p>");
				%>
			</td>
			<td>
				<%
					out.print("<P>" + item.get("category").getS() + "</p>");
				%>
			</td>

			<td><form action="DeleteServlet" method="POST" >
					<input type="hidden" name="picid"	value=<%=item.get("pictureid").getS()%>>
					<%
						if (session.getAttribute("name").equals(
										item.get("username").getS())){
									out.print("<input type='submit' value='Delete'>");
									
						}
					%>
				</form></td>
		</tr>
		<%
			}
			}
		%>

	</table>
</body>
</html>