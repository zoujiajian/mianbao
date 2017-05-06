package com.mianbao.Interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mianbao.common.CacheKey;
import com.mianbao.service.RedisService;
import com.mianbao.util.CookieUtil;
import com.mianbao.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by zoujiajian on 2017-4-8.
 * 用户登录拦截
 */
@Component
public class UserLoginInterceptor implements Filter{

    @Resource
    private RedisService redisService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = request.getRequestURI();
        if(filter(url)){
            Cookie cookie = CookieUtil.getTokenCookie(request);
            if(cookie == null){
                response.sendRedirect("http://localhost:8080/mianbao/travel/index");
            }else{
                String tokenKey = CacheKey.USER_TOKEN + "_" + cookie.getValue();
                String cacheValue = redisService.getByKey(tokenKey);

                if(StringUtils.isNotEmpty(cacheValue)){
                    response.sendRedirect("http://localhost:8080/mianbao/travel/index");
                }else{
                    response.sendRedirect("http://localhost:8080/mianbao/travel/index");
                }
            }
        }else{
            filterChain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {

    }

    private static boolean filter(String url){
        if(StringUtils.isEmpty(url)){
            throw new IllegalArgumentException();
        }
        //访问外网资源不拦截
        if(!url.contains("mianbao")){
            return false;
        }
        //个人中心
        if(url.contains("dynamic/center")){
            return true;
        }
        //收藏景点
        if(url.contains("scenic/collectionInfo")){
            return true;
        }
        //添加景点信息
        if(url.contains("scenic/addScenicSpot")){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String url = "http:/mianbao/travel/user/release.html";
        String[] target = url.split("\\.");
        System.out.println(JSON.toJSONString(target[target.length -1]));
    }
}
