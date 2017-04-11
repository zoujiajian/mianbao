package com.mianbao.facade;

import com.mianbao.domain.UserInfo;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.pojo.user.UserSimpleInfo;
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

    public static UserSimpleInfo userInfoToUserSimpleInfo(UserInfo userInfo){
        UserSimpleInfo userSimpleInfo = new UserSimpleInfo();
        BeanUtils.copyProperties(userInfo,userSimpleInfo);
        return userSimpleInfo;
    }
}
