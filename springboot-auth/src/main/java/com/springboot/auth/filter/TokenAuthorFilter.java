package com.springboot.auth.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author : zx
 * create at:  2019-03-22  02:04
 * @description:
 */
public class TokenAuthorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TokenAuthorFilter  doFilter ======>  start");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("TokenAuthorFilter  doFilter ======>  end");
    }

    @Override
    public void destroy() {

    }
}
