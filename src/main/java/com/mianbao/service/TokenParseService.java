package com.mianbao.service;

import com.alibaba.fastjson.JSON;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zoujiajian on 2017-4-29.
 */
@Service
public class TokenParseService {

    @Resource
    private RedisService redisService;

    public UserLogin getUserLogin(String token){

        String tokenKey = CacheKey.USER_TOKEN + "_" + token;
        String cacheValue = redisService.getByKey(tokenKey);
        if (StringUtils.isEmpty(cacheValue)) {
            return null;
        }
        return JSON.parseObject(cacheValue, UserLogin.class);
    }
}
