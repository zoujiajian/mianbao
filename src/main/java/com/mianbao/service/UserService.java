package com.mianbao.service;


import com.mianbao.common.Result;
import com.mianbao.domain.UserInfo;
import com.mianbao.pojo.user.UserLogin;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoujiajian on 2017-3-20.
 */
public interface UserService {

    /**
     * 注册
     * @param userInfo
     * @return
     */
    Result register(UserInfo userInfo);

    /**
     * 用户登录
     * @param userLogin
     * @return
     */
    Result login(UserLogin userLogin);

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    Result updateUserInfo(UserInfo userInfo);

    /**
     * 用户更新图片信息
     * @param request
     * @return
     */
    Result updateUserPicture(HttpServletRequest request);

    /**
     * 得到用户的基本信息
     * @param userId
     * @return
     */
    Result getUserInfo(Integer userId);

}
