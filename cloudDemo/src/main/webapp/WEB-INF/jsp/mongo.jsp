<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>MongoDB in CloudFoundary</h2>

    <input type="button" value="Clear tables" onclick="window.location='mongo/clear'"/>
    <input type="button" value="Clear counters" onclick="window.location='mongo/clearCounters'"/>
    <br/>    
	<a href="/">home</a>
	<br/>
	Time spent for:
	<table border="1">
		<tr>
			<td><b>Rabbit send: </b>${perfCountRabbitSend/1000} s</td>
			<td><b>Rabbit receive and Redis push: </b>${perfCountRabbitReceive/1000} s</td>
			<td><b>Mongo add: </b>${perfCountMongoAdd} ms</td>
			<td><b>Mongo mapReduce: </b>${perfCountMongoMr} ms</td>
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