package com.springboot.auth.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : zx
 * create at:  2019-03-22  02:29
 * @description:
 */

@Component
public class SimpleInterceptor implements HandlerInterceptor {
    /*
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        System.out.println("SimpleInterceptor  afterCompletion");
    }

    /*
     * 处理请求完成后视图渲染之前的处理操作
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        System.out.println("SimpleInterceptor  postHandle");
    }

    /*
     * 进入controller层之前拦截请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        System.out.println("SimpleInterceptor  preHandle");
//        System.out.println("getContextPath:" + request.getContextPath());
//        System.out.println("getServletPath:" + request.getServletPath());
//        System.out.println("getRequestURI:" + request.getRequestURI());
//        System.out.println("getRequestURL:" + request.getRequestURL());
//        System.out.println("getRealPath:" + request.getSession().getServletContext().getRealPath("image"));
        return true;
    }

}