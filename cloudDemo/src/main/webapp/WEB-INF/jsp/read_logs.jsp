<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>Upload new log file to the system</h2>

	<form action="read_logs/process" method="post" enctype="multipart/form-data">
		<input type="file" name="logFile"/>
		<br/> 
		<input type="submit" value="Process logfile" />
	</form>

	${content}
	<br />
	<a href="/">home</a>
</body>
</html>