<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
    <h2>MongoDB in CloudFoundary</h2>
 
        <form action="save" method="post">
            <input type="hidden" name="id">
            <label for="name">Person Name</label>
            <input type="text" id="name" name="name"/>
            <input type="submit" value="Submit"/>
        </form>
 
    <table border="1">
        <c:forEach var="person" items="${personList}">
            <tr>
                <td>${person.name}</td><td><input type="button" value="delete" onclick="window.location='delete?id=${person.id}'"/></td>
            </tr>
        </c:forEach>
    </table>  
</body>
</html>