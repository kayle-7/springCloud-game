package com.kayle.web.filter;

import com.kayle.web.util.BodyRequestWrapper;
import com.kayle.web.util.BodyResponseWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author : zx
 * create at:  2019-03-22  02:09
 * @description:
 */
@Slf4j
@WebFilter(filterName = "simpleFilter", urlPatterns = "/*")//过滤器执行顺序根据拦截器名（文件名）来排序
public class SimpleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.warn("======>  SimpleFilter  doFilter : start!");
        filterChain.doFilter(servletRequest, servletResponse);
        log.warn("======>  SimpleFilter  doFilter : end!");
    }

    @Override
    public void destroy() {

    }
}
