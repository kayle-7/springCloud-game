package com.springboot.auth.filter;

import com.springboot.auth.util.BodyCachingHttpServletRequestWrapper;
import com.springboot.auth.util.BodyCachingHttpServletResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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

        BodyCachingHttpServletRequestWrapper requestWrapper = new BodyCachingHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        BodyCachingHttpServletResponseWrapper responseWrapper = new BodyCachingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        log.warn("SimpleFilter  doFilter!  ======>>");
        System.out.println("SimpleFilter  doFilter ======>  start!  requestBody : " + new String(requestWrapper.getBody(), StandardCharsets.UTF_8));
        filterChain.doFilter(servletRequest, responseWrapper);
        System.out.println("SimpleFilter  doFilter ======>  end!  responseBody : " + new String(responseWrapper.getBody(), StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {

    }
}
