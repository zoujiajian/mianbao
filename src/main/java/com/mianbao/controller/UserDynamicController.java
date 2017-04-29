package com.mianbao.controller;

import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.DynamicService;
import com.mianbao.service.TokenParseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoujiajian on 2017-4-8.
 */
@RestController
@RequestMapping(value = "/mianbao/travel/dynamic")
public class UserDynamicController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Resource
    private DynamicService dynamicService;

    @Resource
    private TokenParseService parseService;

    @RequestMapping(value = "/release",method = RequestMethod.POST)
    public Result releaseDynamic(HttpServletRequest request){
        Result result;
        try{
            result = dynamicService.releaseDynamic(request);
            if(result.isSuccess()){
                return result;
            }else if(result.getErrorMsg().equals(Response.USER_LOGIN_TIMEOUT.getMsg())){
                return Result.getDefaultError("用户尚未登录");
            }
        }catch (Exception e){
            logger.error("releaseDynamic error",e);
        }
        return Result.getDefaultSuccess(null);
    }

    @RequestMapping(value = "/center",method = RequestMethod.GET)
    public Result getAllDynamic(@CookieValue(name = "token") String token){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return Result.getDefaultError(Response.USER_LOGIN_TIMEOUT.getMsg());
        }
        return  dynamicService.getUserAllDynamic(userLogin.getId());
    }
}
