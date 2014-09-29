<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>RabitMQ in CloudFoundary</h2>

	<form action="rabbit/queue" method="post">
		<input type="hidden" name="id"> 
		<label for="name">Person Name</label> 
		<input type="text" id="name" name="name" /> 
		<input type="submit" value="Submit" />
	</form>

	<h2>${person}</h2>
	
	<input type="button" value="retrive one element from queue and transfer to redis"
		onclick="window.location='rabbit/transfer'" />
	<br />
	<a href="/">home</a>
</body>
</html>