package com.mianbao.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoujiajian on 2017-4-28.
 */
public class CookieUtil {

    private CookieUtil(){


    }

    //获取token对应的信息
    public static Cookie getTokenCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(TokenUtil.TOKEN)){
                    return cookie;
                }
            }
        }
        return null;
    }
}
