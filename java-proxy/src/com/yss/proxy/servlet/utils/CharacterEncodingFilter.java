package com.yss.proxy.servlet.utils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
/**
 * @ClassName: CharacterEncodingFilter
 * @Description:  字符编码过滤器
 * @author: zhengjing
 * @createdate: 2021-04-06 16:39
 */
public class CharacterEncodingFilter implements Filter {

    private String encoding;
    private Boolean forceEncoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        forceEncoding = Boolean.valueOf(filterConfig.getInitParameter("forceEncoding"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (this.encoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
            request.setCharacterEncoding(this.encoding);
            if (this.forceEncoding) {
                response.setCharacterEncoding(this.encoding);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
