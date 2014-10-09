<!DOCTYPE html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
	<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
	<title>EO - Cloud Demo</title>
</head>
<body>
	<h4>Cloud demo</h4>
	<div align="center">
		<table>
			<tr>
				<td>
					<a href="read_netstat">Load and process data ...</a>
				</td>
				<td>
					<a href="stats">Statistics</a>
				</td>
			</tr>
		</table>
	</div>
	<h4>Control panels</h4>
	<div align="center">
		<table>
			<tr>
				<td>
					<a href="mongo">Mongo controller</a>
				</td>
				<td>
					<a href="redis">Redis controller</a>
				</td>
				<td>
					<a href="rabbit">Rabbit controller</a>
				</td>
				<td>
					<a href="logs">Logs</a>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>
