<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<body>
	<h2>MongoDB in CloudFoundary</h2>

	<form action="mongo/save" method="post">
		<input type="hidden" name="id"> 
		<label for="name">Data</label> 
		<input type="text" id="data" name="data" /> 
		<input type="submit" value="Submit" />
	</form>
	<br/>
    <input type="button" value="clear all" onclick="window.location='mongo/clear'"/>
    <input type="button" value="aggregate" onclick="window.location='mongo/aggregate'"/>
    <br/>    
	<a href="/">home</a>
	<br/>

	<h4>Simple data</h4>
	<br/>
	<table border="1">
		<c:forEach var="data" items="${dataList}">
			<tr>
				<td>${data.data}</td>
				<!-- td><input type="button" value="delete"
					onclick="window.location='mongo/delete?id=${data.id}'" /></td-->
			</tr>
		</c:forEach>
	</table>
	<br />

	<h4>Destination hosts</h4>
	<br/>
	<table border="1">
		<c:forEach var="destHost" items="${destHostList}">
			<tr>
				<td>${destHost.id}</td>
				<td>${destHost.name}</td>
				<td>${destHost.value}</td>
				<!-- td><input type="button" value="delete"
					onclick="window.location='mongo/delete?id=${destHost.id}'" /></td-->
			</tr>
		</c:forEach>
	</table>
	<br />

</body>
</html>