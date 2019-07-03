package com.kayle.web.filter;

import com.kayle.web.util.BodyRequestWrapper;
import com.kayle.web.util.BodyResponseWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author : zx
 * create at:  2019-03-22  02:04
 * @description:
 */
@Slf4j
public class TokenAuthorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        BodyRequestWrapper requestWrapper = new BodyRequestWrapper((HttpServletRequest) servletRequest);
        BodyResponseWrapper responseWrapper = new BodyResponseWrapper((HttpServletResponse) servletResponse);
        log.warn("======>  TokenAuthorFilter  doFilter : start!");
        filterChain.doFilter(servletRequest, responseWrapper);
        log.warn("======>  TokenAuthorFilter  doFilter : end!  responseBody : " + new String(responseWrapper.getBody(), StandardCharsets.UTF_8));
    }

    @Override
    public void destroy() {

    }
}
