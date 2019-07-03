package com.springboot.auth.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : zx
 * create at:  2019-03-22  01:52
 * @description:
 */
public class GlobalFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        System.out.println("GlobalFilter  doFilter ======>  start" + requestURI);
        filterChain.doFilter(request, servletResponse);
        System.out.println("GlobalFilter  doFilter ======>  end");
    }

    @Override
    public void destroy() {

    }
}
