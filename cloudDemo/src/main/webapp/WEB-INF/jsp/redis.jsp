<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<h2>Redis in CloudFoundary</h2>
	<a href="/">Home</a>
	<br /><br/>
	<input type="button" value="Flush" onclick="window.location='redis/clear'" />
	<br />
</body>
</html>