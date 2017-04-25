package com.mianbao.Interceptor;

import com.google.common.collect.Sets;
import com.mianbao.common.CacheKey;
import com.mianbao.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by zoujiajian on 2017-4-8.
 * 用户登录拦截
 */
@Component
public class UserLoginInterceptor implements Filter{

    @Resource
    private RedisService redisService;

    /**
     * 需要拦截的url
     */
    private static final Set<String> SPECIAL_URL  = Sets.newHashSet();

    static {
        SPECIAL_URL.add("");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if(SPECIAL_URL.contains(url)){
            String userName = request.getParameter("userName");
            if(StringUtils.isNotEmpty(userName)){
                String cacheKey = CacheKey.USER_LOGIN_PREFIX + "_" + userName;
                String cacheValue = redisService.getByKey(cacheKey);
                if(StringUtils.isEmpty(cacheValue)){
                    request.getRequestDispatcher("sys/index.html").forward(request,response);
                }
            }else{
                request.getRequestDispatcher("sys/index.html").forward(request,response);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
