package com.yss.proxy.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author ktt
 * @Date 2021/7/12 16:12
 **/
/*
 *@ClassName WebFilter
 *@Description TODO
 *@Author ktt
 *@Date 2021/7/12 16:12
 *@Version 1.0
 */
//@javax.servlet.annotation.WebFilter(filterName = "allFilter", urlPatterns = { "/*" })
public class WebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(((HttpServletRequest)servletRequest).getRequestURL().toString());
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
