package com.mianbao.service;


import com.mianbao.common.Result;
import com.mianbao.domain.UserInfo;
import com.mianbao.pojo.user.UserLogin;

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


}
