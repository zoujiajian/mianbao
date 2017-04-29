package com.mianbao.controller;

import com.mianbao.common.CacheKey;
import com.mianbao.domain.UserInfo;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.UserService;
import com.mianbao.util.Md5Util;
import com.mianbao.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zoujiajian on 2017-4-7.
 */
@RestController
@RequestMapping(value = "/mianbao/travel/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody UserLogin userLogin){
        if(StringUtils.isEmpty(userLogin.getUserName()) ||
                StringUtils.isEmpty(userLogin.getUserPassword())){
            return Result.getDefaultError(Response.LOGIN_FAIL.getMsg());
        }
        Result result;
        try{
            result = userService.login(userLogin);
            if(result.isSuccess()){
               //返回token
               return Result.getDefaultSuccess(TokenUtil.userInfoToToken(userLogin));
            }
        }catch (Exception e){
            logger.error("用户登录错误: ",e);
            return Result.getDefaultError(Response.LOGIN_FAIL.getMsg());
        }
        return result;
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Result register(@RequestBody UserInfo userInfo){
        Result result;
        try{
            if(userInfo.getId() != null){
                return Result.getDefaultSuccess(Response.REGION_FAIL.getMsg());
            }
            result = userService.register(userInfo);
        }catch (Exception e){
            logger.error("用户注册失败: ",e);
            return Result.getDefaultSuccess(Response.REGION_FAIL.getMsg());
        }
        return result;
    }

    @RequestMapping(value = "/updatePicture")
    public Result updatePicture(HttpServletRequest request){
        String userId = request.getParameter("userId");
        if(StringUtils.isEmpty(userId)){
            return Result.getDefaultError(Response.UPDATE_PICTURE.getMsg());
        }
        Result result;
        try{
            result = userService.updateUserPicture(request);
        }catch (Exception e){
            logger.error("用户上传图片失败 : ",e);
            return Result.getDefaultError(Response.UPDATE_PICTURE.getMsg());
        }
        return result;
    }

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public Result getUserInfo(@RequestParam("userId") Integer userId){
        Result result;
        try{
            result = userService.getUserInfo(userId);
        }catch (Exception e){
            logger.error("得到用户基本信息失败 :",e);
            return Result.getDefaultError(Response.GET_USER_INFO.getMsg());
        }
        return result;
    }

    @RequestMapping(value = "/updateUserInfo",method = RequestMethod.POST)
    public Result updateUserInfo(@RequestBody UserInfo userInfo){
        Result result;
        try{
            result = userService.updateUserInfo(userInfo);
        }catch (Exception e){
            logger.error("更新用户详细信息失败",e);
            return Result.getDefaultError(Response.UPDATE_USER_INFO_FAIL.getMsg());
        }
        return result;
    }
}
