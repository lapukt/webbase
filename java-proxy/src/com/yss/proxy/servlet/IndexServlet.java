package com.yss.proxy.servlet;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yss.proxy.servlet.utils.ProfileConfigUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName: IndexServlet
 * @Description: Index Servlet
 * @author: zhengjing
 * @createdate: 2021-02-24 13:20
 */
//@WebServlet("/main")
public class IndexServlet extends HttpServlet {

    /**
     * 用户标识key
     */
    private final static String SM_USER = "sm_user";
    /**
     * 测试用户名
     */
    private final static String USER_NAME = "testUserName";
    /**
     * SSO客户标识
     */
    private final static String SSO_CUST = "hqyh";

    private String smUserKey;
    private String testUserName;


    @Override
    public void init(ServletConfig config) throws ServletException {
        smUserKey = config.getInitParameter(SM_USER);
        testUserName = config.getInitParameter(USER_NAME);
        super.init(config);
    }

    /**
     * @param request
     * @param response
     * @return void
     * @methodName: service
     * @description: 请求业务处理
     * @date: 2021-02-24 11:46
     * @author: zhengjing
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            // 获取SM_USER头部参数
            String smUser = request.getHeader(smUserKey);
            // 测试用户进行测试
            if (testUserName != null && !"".equals(testUserName)) {
                smUser = testUserName;
            }
            System.out.println("===============>get header user info value[SM_USER]:" + smUser);
            if (smUser != null && !"".equals(smUser)) {
                // 跳转报表世界
                String userName = Base64.encode(smUser.getBytes());
                Cookie cookie1 = new Cookie("sso_ltpa_name", userName);
                Cookie cookie2 = new Cookie("sso_ltpa_cust_from", SSO_CUST);
                Cookie cookie3 = new Cookie("sso_ltpa_login_url", ProfileConfigUtil.SSO_LOGIN_URL);
                response.addCookie(cookie1);
                response.addCookie(cookie2);
                response.addCookie(cookie3);
                response.sendRedirect("dist/index.html?ssofrom="+SSO_CUST);
            } else {
                System.out.println("===============>request header user info value[SM_USER] is blank.");
                // 错误页面
                printErrorPage(request, response, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
            printErrorPage(request, response, out);
        } finally {
            out.close();
        }
    }

    /**
     * @param request
     * @param response
     * @param out
     * @return void
     * @methodName: printErrorPage
     * @description: 错误页面
     * @date: 2021-02-24 11:46
     * @author: zhengjing
     */
    private void printErrorPage(HttpServletRequest request, HttpServletResponse response, PrintWriter out) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        out.println("<html>");
        out.println("<head><title>错误提示</title></head>");
        out.println("<body>");
        out.println("<table style='margin: 0 auto; margin-top:100px; width:500px; padding:30px'>");
        out.println("<tr><td><h2>请求登录认证异常,请联系管理员.</h2></td></tr>");
        out.println("<tr><td>异常信息:无法获取SM_USER...</td></tr>");
        out.println("<tr><td>错误码:403 <a href='" + ProfileConfigUtil.SSO_LOGIN_URL + "' alt='统一登陆请求地址' >返回登录页面</a></td></tr>");
        out.println("</table>");
        out.println("</body></html>");
    }
}
