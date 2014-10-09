<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<h2>Logs</h2>
    <br/>
	<input type="button" value="clear logs" onclick="window.location='logs/clear'"/>    
	<br/>
	<a href="/">home</a>
	<br/><br/>
	${logs}
</body>
</html>