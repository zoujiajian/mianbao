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

    private static final Map<String,Set<String>> SOURCE = Maps.newHashMap();

    static {

        Set<String> var1 = Sets.newHashSet();
        var1.add("login");
        var1.add("register");
        SOURCE.put("user",var1);

        Set<String> var2 = Sets.newHashSet();
        var2.add("getAllScenicList");
        SOURCE.put("scenic",var2);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        /*String url = request.getRequestURI();
        if(filter(url)){
            Cookie cookie = CookieUtil.getTokenCookie(request);
            if(cookie == null){
                request.getRequestDispatcher("index.html").forward(request,response);
            }else{
                String tokenKey = CacheKey.USER_TOKEN + "_" + cookie.getValue();
                String cacheValue = redisService.getByKey(tokenKey);

                if(StringUtils.isNotEmpty(cacheValue)){
                    request.getRequestDispatcher("index.html").forward(request,response);
                }else{
                    request.getRequestDispatcher("index.html").forward(request,response);
                }
            }
        }*/
        filterChain.doFilter(request, response);
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
        String[] sources = url.split("\\.");
        String sourceSuffix = sources[sources.length - 1];
        if(sourceSuffix.equals("css") || sourceSuffix.equals("js") || sourceSuffix.equals("ico")){
           return false;
        }
        //只拦截针对user路径的访问
        String[] source = url.split("/");
        if(SOURCE.containsKey(source[3])){
            return SOURCE.get(source[3]).contains(source[4]);
        }
        return false;
    }

    public static void main(String[] args) {
        String url = "http:/mianbao/travel/user/release.html";
        String[] target = url.split("\\.");
        System.out.println(JSON.toJSONString(target[target.length -1]));
    }
}
