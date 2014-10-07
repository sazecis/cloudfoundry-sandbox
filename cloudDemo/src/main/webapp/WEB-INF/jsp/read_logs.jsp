<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>Process log files</h2>

	<form action="read_logs/process" method="post" enctype="multipart/form-data">
		<input type="file" name="logFile"/>
		<br/> 
		<input type="submit" value="Upload & Process" />
		<input type="submit" value="Upload only" />
	</form>

	${content}
	<br />
	<a href="/">home</a>
	<br/>
	<table border="1">
		<c:forEach var="file" items="${fileList}">
			<tr>
				<td><input type="checkbox" name="${file.name}" ${file.checked} onclick="window.location='read_logs/mark'" />${file.name}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<input type="submit" value="Upload selected" />
</body>
</html>