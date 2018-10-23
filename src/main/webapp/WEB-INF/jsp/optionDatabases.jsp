<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

    <option value="">Select A Database</option>
<c:forEach var="list" items="${dbLists}">
    <option value="${list.Database}">${list.Database}</option>
 </c:forEach>