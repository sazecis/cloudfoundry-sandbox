<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
    <h2>Redis in CloudFoundary</h2>
 
        <form action="redis/save" method="post">
            <label for="name">Person Name</label>
            <input type="text" id="name" name="name"/>
            <input type="submit" value="Submit"/>
        </form>
 
    <table border="1">
        <c:forEach var="person" items="${personList}">
            <tr>
                <td>${person}</td>
            </tr>
        </c:forEach>
    </table>  
    <input type="button" value="transfer one to mongo" onclick="window.location='redis/transfer'"/>
    <br/>    
    <a href="/">home</a>      
</body>
</html>