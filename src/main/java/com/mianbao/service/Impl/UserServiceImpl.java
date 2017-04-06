package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.dao.UserInfoMapper;
import com.mianbao.domain.UserInfo;
import com.mianbao.domain.UserInfoExample;
import com.mianbao.enums.Response;
import com.mianbao.exception.BusinessException;
import com.mianbao.facade.UserInfoFacade;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.UserService;
import com.mianbao.service.RedisService;
import com.mianbao.util.FileLoadUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * Created by zoujiajian on 2017-3-21.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisService redisService;

    @Override
    public Result register(UserInfo userInfo) {

        if(StringUtils.isEmpty(userInfo.getUserName()) ||
             StringUtils.isEmpty(userInfo.getUserPassword()) ||
                StringUtils.isEmpty(userInfo.getUserSex())){
            throw new BusinessException(Response.REGION_FAIL.getMsg(),Response.REGION_FAIL.getCode());
        }
        int record = userInfoMapper.insert(userInfo);
        if(record > 0){
            String cacheKey = CacheKey.USER_LOGIN_PREFIX + "_" + userInfo.getUserId();
            redisService.addByKeyWithExpire(cacheKey,JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.REGION_FAIL.getMsg());
    }

    @Override
    public Result login(UserLogin userLogin) {

        if(userLogin == null || StringUtils.isEmpty(userLogin.getUserName()) ||
                StringUtils.isEmpty(userLogin.getPassword())){
           throw new BusinessException(Response.LOGIN_FAIL.getMsg(),Response.LOGIN_FAIL.getCode());
        }
        String userLoginInfo = redisService.getByKey(userLogin.getUserName());
        if(StringUtils.isNotEmpty(userLoginInfo)){
            UserLogin loginInfo = JSON.parseObject(userLoginInfo,UserLogin.class);
            if(loginInfo.getPassword().equals(userLogin.getPassword())){
                return Result.getDefaultSuccess(null);
            }
        }

        UserInfoExample example = new UserInfoExample();
        example.createCriteria().andUserNameEqualTo(userLogin.getUserName()).
                                 andUserPasswordEqualTo(userLogin.getPassword());
        List<UserInfo> login = userInfoMapper.selectByExample(example);
        UserInfo userInfo = login.get(0);
        if(userInfo.getUserName().equals(userLogin.getUserName()) &&
                userInfo.getUserPassword().equals(userLogin.getPassword())){
            String cacheKey = CacheKey.USER_LOGIN_PREFIX + "_" + userInfo.getUserId();
            redisService.addByKeyWithExpire(cacheKey,JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.LOGIN_FAIL.getMsg());
    }

    @Override
    public Result updateUserInfo(UserInfo userInfo) {
        if(userInfo == null || userInfo.getUserId() == null){
            throw new BusinessException(Response.UPDATE_USER_INFO_FAIL.getMsg(),Response.UPDATE_USER_INFO_FAIL.getCode());
        }
        int count = userInfoMapper.updateByPrimaryKey(userInfo);
        if(count > 0){
            String cacheKey = CacheKey.USER_INFO_PREFIX + "_" + userInfo.getUserId();
            redisService.addByKeyWithExpire(cacheKey, JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.UPDATE_USER_INFO_FAIL.getMsg());
    }

    @Override
    public Result updateUserPicture(HttpServletRequest request) {
        String picture = FileLoadUtil.load(request);
        if(StringUtils.isEmpty(picture)){
            return Result.getDefaultError(Response.UPDATE_PICTURE.getMsg());
        }

        String userId = request.getParameter("userId");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Integer.valueOf(userId));
        userInfo.setUserPicutre(picture);
        int count = userInfoMapper.updateByPrimaryKey(userInfo);
        if(count > 0){
            String cacheKey = CacheKey.USER_PICTURE_PREFIX + "_" + userInfo.getUserId();
            redisService.addByKeyWithExpire(cacheKey,JSON.toJSONString(picture),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError((Response.UPDATE_PICTURE.getMsg()));
    }

    @Override
    public Result getUserInfo(Integer userId) {
        if(userId == null || userId < 0){
            throw new BusinessException(Response.GET_USER_INFO.getMsg(),Response.GET_USER_INFO.getCode());
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        return Result.getDefaultSuccess(UserInfoFacade.UserInfoToVo(userInfo));
    }
}
