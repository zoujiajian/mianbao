package com.mianbao.facade;

import com.mianbao.domain.UserInfo;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;

/**
 * Created by zoujiajian on 2017-4-6.
 */
public class UserInfoFacade {

    private UserInfoFacade(){

    }

    public static UserInfoVo UserInfoToVo(UserInfo userInfo){
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,vo);
        return vo;
    }

    public static UserLogin userInfoToUserLogin(UserInfo userInfo){
        UserLogin userLogin = new UserLogin();
        BeanUtils.copyProperties(userInfo,userLogin);
        return userLogin;
    }
}
