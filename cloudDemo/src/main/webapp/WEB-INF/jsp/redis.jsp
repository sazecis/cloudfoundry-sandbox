<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>Redis in CloudFoundary</h2>

	<input type="button" value="Flush" onclick="window.location='redis/clear'" />
	<br />
	<a href="/">home</a>
	<br />
	<h4>Current Redis state</h4>
	<table border="1">
		<c:forEach var="netStat" items="${netStatsList}">
			<tr>
				<td>${netStat}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>