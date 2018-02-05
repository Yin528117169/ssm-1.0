<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/10/7
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>List Page</title>
    <script type="text/javascript">
        $(function(){
            $(".delete").click(function(){
                var href = $(this).attr("href");
                $("form").attr("action",href).submit();
                return false;
            })
        })
    </script>
</head>
<body>

<c:if test="${empty requestScope.list }">
    No any list!
</c:if>

<c:if test="${!empty requestScope.list }">
    <table border="1" cellpadding="10" cellspacing="0">
        <tr>
            <th>BookID</th>
            <th>Name</th>
            <th>Number</th>
        </tr>

        <c:forEach items="${requestScope.list }" var="book">
            <tr>
                <td>${book.bookId }</td>
                <td>${book.name }</td>
                <td>${book.number }</td>
            </tr>
        </c:forEach>
    </table>
</c:if>

</body>
</html>
