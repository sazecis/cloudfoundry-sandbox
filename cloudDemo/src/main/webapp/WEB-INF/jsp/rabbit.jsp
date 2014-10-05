<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>RabitMQ in CloudFoundary</h2>

	<form action="rabbit/queue" method="post">
		<input type="hidden" name="id"> 
		<label for="name">Data</label> 
		<input type="text" id="data" name="data" /> 
		<input type="submit" value="Submit" />
	</form>

	<h2>${data}</h2>
	
	<input type="button" value="transfer one to redis"
		onclick="window.location='rabbit/transfer'" />
	<input type="button" value="purge"
		onclick="window.location='rabbit/purge'" />
	<input type="checkbox" name="DevNull" value="DevNull" ${state} onclick="window.location='rabbit/nulldev'" /> DevNull
	<br />
	<a href="/">home</a>
</body>
</html>