<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<h2>RabitMQ in CloudFoundary</h2>

	<input type="button" value="Purge" onclick="window.location='rabbit/purge'" />
	<input type="checkbox" name="DevNull" value="DevNull" ${state} onclick="window.location='rabbit/nulldev'" /> DevNull
	<br />
	<a href="/">home</a>
</body>
</html>