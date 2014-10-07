<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>MongoDB in CloudFoundary</h2>

    <input type="button" value="clear all" onclick="window.location='mongo/clear'"/>
    <br/>    
	<a href="/">home</a>
	<br/>
	Time spent for:
	<table>
		<tr>
			<td>Rabbit send: ${perfCountRabbitSend}</td>
			<td>Rabbit receive: ${perfCountRabbitReceive}</td>
			<td>Mongo add: ${perfCountMongoAdd}</td>
			<td>Mongo MapReduce: ${perfCountMongoMr}</td>
		</tr>
	</table>
	<h4>Collected according to destination hosts</h4>
	<br/>
	<table border="1">
		<c:forEach var="destHost" items="${destHostList}">
			<tr>
				<td>${destHost.name}</td>
				<td>${destHost.value}</td>
			</tr>
		</c:forEach>
	</table>
	<br />

	<h4>Collected according to log entry time stamps</h4>
	<br/>  
	<table border="1">
		<c:forEach var="logDate" items="${logDataList}">
			<tr>
				<td>${logDate.timeStampAsString}</td>
				<td>${logDate.value}</td>
			</tr>
		</c:forEach>
	</table>
	<br />

</body>
</html>