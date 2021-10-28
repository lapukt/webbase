<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021-01-19
  Time: 16:20
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.io.PrintStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>500 服务器内部错误</title>
</head>
<body>
<div class="ui-alert-panel">
    <h1>服务器内部错误</h1>
    <p>处理您的请求时发生错误,请联系管理员</p>
</div>
<div >
    <% //此处输出异常信息
        exception.printStackTrace();
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(ostr));
        PrintWriter out1 = response.getWriter();
        out1.print(ostr);
    %>
</div>
</body>
</html>
