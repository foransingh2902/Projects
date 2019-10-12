<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Tracker App</title>
</head>
<body>

	<div>
		<h2>BTES</h2>
		<input type="button" value="Add Student"
			onclick="window.location.href='add-student-form.jsp' ">
	</div>
	<div>
		<table>
			<tr>
				<th>First Name</th>
				<th>Last Name</th>
				<th>Email</th>
				<th>Action</th>
			</tr>
			<!-- JSTL tag will got to the response object and retrieve info about STUDENT_LIST -->
			<c:forEach var="tempStudent" items="${STUDENT_LIST}">
				<!-- set up a link for each student -->
				<c:url var="templink" value="StudentControllerServlet">
					<c:param name="command" value="LOAD"></c:param>
					<c:param name="studentId" value="${ tempStudent.id}"></c:param>
				</c:url>
				<!-- set up a link to delete a student -->
				<c:url var="deletelink" value="StudentControllerServlet">
					<c:param name="command" value="DELETE"></c:param>
					<c:param name="studentId" value="${ tempStudent.id}"></c:param>
				</c:url>
				<tr>
					<td>${tempStudent.firstname}</td>
					<td>${tempStudent.lastname}</td>
					<td>${tempStudent.email}</td>
					<td>
					<a href="${templink}">Update</a>
					| <a href="${deletelink}" onclick="if(!(confirm('Are you sure you want to delete this student?')))return false">Delete</a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>