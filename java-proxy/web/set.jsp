<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>服务设置</title>
</head>
<body>
<div >
    <h2>环境配置
        <span style="font-size: 14px;">[当前服务器IP:<%=request.getServerName()+":"+request.getServerPort()%>]</span>
    </h2>
    <div><b>说明:</b></div>
    <span style="font-size: 14px;">支持环境： dev开发环境\uat测试环境\pro生产环境<br/>
       config.profile： 环境切换<br/>
       proxy.getway： 网关地址<br/>
       proxy.login： 统一门户登录地址</br>
    </span>
    <div style="font-size: 14px;color: red;">
      提示：修改完配置后,需要重启服务才能生效!!!
    </div>

</div>
<div>
    <form action="<%=request.getServletContext().getContextPath() + "/config?exc=save" %>" method="post">
        <table>
            <tr>
                <td>
                    <textarea name="content" rows="20" cols="100"><%= request.getAttribute("data") %></textarea>
                </td>

            </tr>
            <tr>
                <td>
                    <input type="submit" value="提交"/>
                </td>
            </tr>
        </table>
    </form>

</div>
</body>
</html>
<script type="text/javascript">
    <% if(request.getAttribute("msg") != null && !"".equals(request.getAttribute("msg"))){%>
    alert('<%=request.getAttribute("msg")%>')
    <% }%>
</script>