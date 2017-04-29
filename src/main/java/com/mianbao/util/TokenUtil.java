package com.mianbao.util;

import com.mianbao.pojo.user.UserLogin;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zoujiajian on 2017-4-28.
 */
public class TokenUtil {

    private TokenUtil(){


    }

    public static final String TOKEN = "token";

    /**
     * 使用用户登录信息构建token
     * @param userLogin
     * @return
     */
    public static String userInfoToToken(UserLogin userLogin){
        if(userLogin == null || StringUtils.isEmpty(userLogin.getUserName()) ||
                StringUtils.isEmpty(userLogin.getUserPassword())){
            throw new IllegalArgumentException();
        }
        String tokenKey = userLogin.getUserName() + "_" + userLogin.getUserPassword();
        return  Md5Util.getMD5Key(tokenKey);
    }
}
