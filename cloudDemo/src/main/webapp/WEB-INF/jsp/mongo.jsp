<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
</head>
<body>
	<h2>MongoDB in CloudFoundary</h2>
	<a href="/">Home</a>
	<br/><br/>
    <input type="button" value="Clear tables" onclick="window.location='mongo/clear'"/>
    <input type="button" value="Clear counters" onclick="window.location='mongo/clearCounters'"/>
    <br/>    
	<br/>
	<table border="1">
		<tr>
			<td><b>Rabbit send: </b>${perfCountRabbitSend/1000} s</td>
			<td><b>Rabbit receive and Redis push: </b>${perfCountRabbitReceive/1000} s</td>
			<td><b>Mongo add: </b>${perfCountMongoAdd/1000} s</td>
			<td><b>Mongo mapReduce: </b>${perfCountMongoMr/1000} s</td>
		</tr>
	</table>
	<h4>Collected according to destination hosts</h4>
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