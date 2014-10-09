<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<h2>Process Net Statistics files</h2>
	<br />
	<a href="/">home</a>
	<br /><br />
	<b>Upload and process the selected file (create statistics)</b>
	<br />
	<form action="read_netstat/live_process" method="post"
		enctype="multipart/form-data">
		<input type="file" name="logFile" /> <br /> <input type="submit"
			value="Upload & Process" />
	</form>
	<b>Upload and store as a raw file on the server</b>
	<br />
	<form action="read_netstat/upload" method="post"
		enctype="multipart/form-data">
		<input type="file" name="logFile" /> <br /> <input type="submit"
			value="Upload" />
	</form>
	<br /><br />
	<b>Raw files on the server</b>
	<br />
	<table border="1">
		<tr>
			<td><b>FileName</b></td>
			<td><b>Size</b></td>
			<td><b>Process it</b></td>
		</tr>	
		<c:forEach var="uploadedFile" items="${fileList}">
			<tr>
				<td>${uploadedFile.name}</td>
				<td>${uploadedFile.size}</td>
				<td>
					<input type="button" value="Process it"
						onclick="window.location='read_netstat/process_selected?name=${uploadedFile.name}'" /> 
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>