<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%!
    
    	private static final String DEFAULT_USER="Guest";
    
    %>
    
    <%
    String user=request.getParameter("user");
    if(user == null)
    	user=DEFAULT_USER;
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Hello User Application</title>
</head>
<body>

Hello , <%= user %> <br/>
<form action="Greeting.jsp" method="post">
Enter Your Name: <br/>
<input type="text" name="user"/><br/>
<input type="submit" value="Submit"/>
</form>

</body>
</html>