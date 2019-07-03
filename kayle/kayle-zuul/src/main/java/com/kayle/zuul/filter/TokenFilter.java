package com.kayle.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : zx
 * create at:  2019-03-24  18:19
 * @description:
 */
@Component
public class TokenFilter extends ZuulFilter {

    //过滤器类型     pre表示请求之前进行执行
    @Override
    public String filterType() {
        return "pre";
    }

    //过滤器执行顺序，当一个请求在一个阶段的时候，存在多个过滤器的时候，多个过滤器执行顺序
    @Override
    public int filterOrder() {
        return 0;
    }

    //判断过滤器是否生效
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //编写过滤器拦截业务逻辑代码
    @Override
    public Object run() throws ZuulException {
        //拦截所有的服务接口，判断服务接口上是否有传递userToken参数

        //获取上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = requestContext.getRequest();
        //获取token
        String userToken = request.getParameter("userToken");
        if (StringUtils.isEmpty(userToken)) {
            //不会继续执行。。。不会去调用服务接口，网关服务直接相应给客户端
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseBody("userToken is null");
            requestContext.setResponseStatusCode(401);
            return null;
            //返回一个错误提示
        }
        //正常执行调用其他服务接口。。。
        return null;
    }
}
