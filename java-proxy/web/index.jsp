<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021-01-19
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>欢迎...</title>
</head>
<body>
<%
  request.getRequestDispatcher("/main").forward(request,response);
%>
</body>

</html>
