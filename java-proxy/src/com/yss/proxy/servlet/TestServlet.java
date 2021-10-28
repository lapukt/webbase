package com.yss.proxy.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @ClassName: HuaQiServlet
 * @Description: 测试类(查看请求头及上下文信息)
 * @author: zhengjing
 * @createdate: 2021-02-03 17:26
 */
//@WebServlet("/test")
public class TestServlet extends HttpServlet {


    /**
     * @methodName: service
     * @description: 测试 查看头信息、上下文相关信息
     * @param request
     * @param response
     * @return void
     * @date: 2021-02-24 13:30
     * @author: zhengjing
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head>");
        out.println("<title>Servlet测试</title></head>");
        out.println("<body>");


        //获取所有的头部参数
        Enumeration<String> headerNames = request.getHeaderNames();
        out.println("<b style='color:red;background-color: yellow;'>1.请求头参数:request.getHeaderNames()</b><br><hr>");
        System.out.println("=======request Header info=======");
        for (Enumeration<String> e = headerNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getHeader(thisName);
            System.out.println("header的key:" + thisName + "--------------header的value:" + thisValue);
            out.println(thisName + "=>" + thisValue + "<br><hr>");
        }
        //获取所有的请求参数
        Enumeration<String> paraNames = request.getParameterNames();
        out.println("<b style='color:red;background-color: yellow;'>2.请求参数:request.getParameterNames()</b><br><hr>");
        System.out.println("=======request Parameter info=======");
        for (Enumeration<String> e = paraNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            System.out.println("param的key:" + thisName + "--------------param的value:" + thisValue);
            out.println(thisName + "=>" + thisValue + "<br><hr>");
        }
        // ServletContext 上下文参数
        Enumeration<String> attributeNames = request.getServletContext().getAttributeNames();
        out.println("<b style='color:red;background-color: yellow;'>3.ServletContext上下文参数信息:request.getServletContext().getAttributeNames()</b><br><hr>");
        System.out.println("======= ServletContext parameters info =======");
        for (Enumeration<String> e = attributeNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            System.out.println("param的key:" + thisName + "--------------param的value:" + thisValue);
            out.println(thisName + "=>" + thisValue + "<br><hr>");
        }
        // ServletContext 初始化参数
        Enumeration<String> initParameterNames = request.getServletContext().getInitParameterNames();
        out.println("<b style='color:red;background-color: yellow;'>4.ServletContext上下文初始化参数信息:request.getServletContext().getInitParameterNames()</b><br><hr>");
        System.out.println("======= ServletContext init parameters info =======");
        for (Enumeration<String> e = initParameterNames; e.hasMoreElements(); ) {
            String thisName = e.nextElement().toString();
            String thisValue = request.getParameter(thisName);
            System.out.println("param的key:" + thisName + "--------------param的value:" + thisValue);
            out.println(thisName + "=>" + thisValue + "<br><hr>");
        }
        out.println("</body></html>");
    }
}
