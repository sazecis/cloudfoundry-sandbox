<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
<p>
	<h2>Redis in CloudFoundary</h2>

	<form action="redis/save" method="post">
		<label for="name">Data</label> 
		<input type="text" id="data" name="data" /> 
		<input type="submit" value="Submit" />
	</form>
</p>
<p>
	<input type="button" value="transfer one to mongo"
		onclick="window.location='redis/transfer'" />
	<br />
	<input type="button" value="clear data"
		onclick="window.location='redis/clear'" />
	<br />
	<a href="/">home</a>
</p>
<p>
	<h4>Simplem data table. Key: myData</h4>
	<table border="1">
		<c:forEach var="data" items="${dataList}">
			<tr>
				<td>${data}</td>
			</tr>
		</c:forEach>
	</table>
</p>
<p>
	<h4>Destination host table. Key: DestHost</h4>
	<table border="1">
		<c:forEach var="destHost" items="${destHostList}">
			<tr>
				<td>${destHost}</td>
			</tr>
		</c:forEach>
	</table>
</p>
</body>
</html>