package com.mianbao.service.Impl;

import com.mianbao.common.Result;
import com.mianbao.dao.UserInfoMapper;
import com.mianbao.domain.UserInfo;
import com.mianbao.enums.Response;
import com.mianbao.exception.BusinessException;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.UserService;
import com.mianbao.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zoujiajian on 2017-3-21.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisService redisService;

    private static final int DEFAULT_EXPIRE = 1000 * 60 * 60 * 24;

    @Override
    public Result register(UserInfo userInfo) {
        if(StringUtils.isEmpty(userInfo.getUserName()) ||
             StringUtils.isEmpty(userInfo.getUserPassword()) ||
                StringUtils.isEmpty(userInfo.getUserSex())){
            throw new BusinessException(Response.REGION_FAIL.getMsg(),Response.REGION_FAIL.getCode());
        }
        int record = userInfoMapper.insert(userInfo);
        if(record > 0){
            //缓存用户登录信息
            redisService.addByKeyWithExpire(userInfo.getUserName(),userInfo.getUserPassword(),DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.REGION_FAIL.getMsg());
    }

    @Override
    public Result login(UserLogin userLogin) {
        return null;
    }


}
