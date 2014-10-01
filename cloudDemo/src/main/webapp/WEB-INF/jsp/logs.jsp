<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
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