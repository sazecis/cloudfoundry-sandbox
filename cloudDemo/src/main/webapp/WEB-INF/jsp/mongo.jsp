<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>MongoDB in CloudFoundary</h2>

	<form action="mongo/save" method="post">
		<input type="hidden" name="id"> 
		<label for="name">Data</label> 
		<input type="text" id="name" name="name" /> 
		<input type="submit" value="Submit" />
	</form>

	<table border="1">
		<c:forEach var="data" items="${dataList}">
			<tr>
				<td>${data.name}</td>
				<td><input type="button" value="delete"
					onclick="window.location='mongo/delete?id=${data.id}'" /></td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<a href="/">home</a>
</body>
</html>