<%@page import="com.seclore.main.domain.UserDetails"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin DashBoard</title>
</head>
<body>
<%UserDetails user = (UserDetails) session.getAttribute("loggedInUser");
if(user.getPosition()!="admin"){
	response.sendRedirect("userdashboard.jsp");	
}
%>
	<table>
	<tr><form:form action = "updatepassword.jsp" ><td>change password</td><td><input type = "submit" value="update"></td></form:form></tr>
	<tr><form:form action = "updateinfo.jsp"><td>change user information</td><td><input type = "submit" value="update"></td></form:form></tr>
	<tr><form:form action = "addUser.jsp"><td>Add new user</td><td><input type = "submit" value="add new user"></td></form:form></tr>
	<tr><form:form action = "addroom.jsp"><td></td>Add Room<td><input type = "submit" value="submit"></td></form:form></tr>
	</table>

</body>
</html>