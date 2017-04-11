package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.mianbao.domain.UserInfo;
import com.mianbao.domain.UserInfoExample;
import com.mianbao.common.CacheKey;
import com.mianbao.common.DefaultField;
import com.mianbao.common.Result;
import com.mianbao.dao.UserInfoMapper;
import com.mianbao.enums.Response;
import com.mianbao.exception.BusinessException;
import com.mianbao.facade.UserInfoFacade;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.UserService;
import com.mianbao.service.RedisService;
import com.mianbao.service.FileLoadService;
import com.mianbao.util.Md5Util;
import org.apache.commons.collections.CollectionUtils;
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
        if(userNameIsRegister(userInfo.getUserName())){
            return Result.getDefaultError(Response.REGION_FAIL_USER_NAME.getMsg());
        }
        //密码使用MD5 加密
        userInfo.setUserPassword(Md5Util.getMD5Key(userInfo.getUserPassword()));
        int record = userInfoMapper.insertSelective(userInfo);
        String cacheKey = CacheKey.USER_INFO_PREFIX + "_" + userInfo.getId();
        if(record > 0){
            redisService.addByKeyWithExpire(cacheKey,JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.REGION_FAIL.getMsg());
    }

    @Override
    public Result login(UserLogin userLogin) {

        if(userLogin == null || StringUtils.isEmpty(userLogin.getUserName()) ||
                StringUtils.isEmpty(userLogin.getUserPassword())){
           throw new BusinessException(Response.LOGIN_FAIL.getMsg(),Response.LOGIN_FAIL.getCode());
        }
        userLogin.setUserPassword(Md5Util.getMD5Key(userLogin.getUserPassword()));
        String cacheKey = CacheKey.USER_LOGIN_PREFIX + "_" + userLogin.getUserName();
        String userLoginInfo = redisService.getByKey(cacheKey);
        if(StringUtils.isNotEmpty(userLoginInfo)){
            UserLogin login = JSON.parseObject(userLoginInfo,UserLogin.class);
            if(login.getUserPassword().equals(userLogin.getUserPassword())){
                return Result.getDefaultSuccess(null);
            }
        }

        UserInfoExample example = new UserInfoExample();
        example.createCriteria().andUserNameEqualTo(userLogin.getUserName()).
                                 andUserPasswordEqualTo(userLogin.getUserPassword());
        List<UserInfo> login = userInfoMapper.selectByExample(example);
        UserInfo userInfo = login.get(0);
        UserLogin loginInfo = UserInfoFacade.userInfoToUserLogin(userInfo);
        if(userInfo != null){
            redisService.addByKeyWithExpire(cacheKey,JSON.toJSONString(loginInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.LOGIN_FAIL.getMsg());
    }

    @Override
    public Result updateUserInfo(UserInfo userInfo) {
        if(userInfo == null || userInfo.getId() == null){
            throw new BusinessException(Response.UPDATE_USER_INFO_FAIL.getMsg(),Response.UPDATE_USER_INFO_FAIL.getCode());
        }

        if(StringUtils.isNotEmpty(userInfo.getUserName()) && userNameIsRegister(userInfo.getUserName())){
            return Result.getDefaultError(Response.REGION_FAIL_USER_NAME.getMsg());
        }
        if(StringUtils.isNotEmpty(userInfo.getUserPassword())){
            userInfo.setUserPassword(Md5Util.getMD5Key(userInfo.getUserPassword()));
        }
        int count = userInfoMapper.updateByPrimaryKeySelective(userInfo);
        if(count > 0){
            String cacheKey = CacheKey.USER_INFO_PREFIX + "_" + userInfo.getId();
            redisService.addByKeyWithExpire(cacheKey, JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.UPDATE_USER_INFO_FAIL.getMsg());
    }

    @Override
    public Result updateUserPicture(HttpServletRequest request) {
        String picture = FileLoadService.load(request);
        if(StringUtils.isEmpty(picture)){
            return Result.getDefaultError(Response.UPDATE_PICTURE.getMsg());
        }

        String userId = request.getParameter("userId");
        UserInfo userInfo = new UserInfo();
        userInfo.setId(Integer.valueOf(userId));
        userInfo.setUserPicture(picture);
        int count = userInfoMapper.updateByPrimaryKey(userInfo);
        if(count > 0){
            String cacheKey = CacheKey.USER_PICTURE_PREFIX + "_" + userInfo.getId();
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
        String cacheKey = CacheKey.USER_INFO_PREFIX + "_" + userId;
        String infoValue = redisService.getByKey(cacheKey);
        if(StringUtils.isNotEmpty(infoValue)){
            UserInfo userInfo = JSON.parseObject(infoValue,UserInfo.class);
            return Result.getDefaultSuccess(UserInfoFacade.UserInfoToVo(userInfo));
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        //如果用户头像为空 则赋予默认头像
        if(StringUtils.isEmpty(userInfo.getUserPicture())){
            userInfo.setUserPicture(DefaultField.DEFAULT_USER_PICTURE);
        }
        return Result.getDefaultSuccess(UserInfoFacade.UserInfoToVo(userInfo));
    }

    /**
     * 判断该用户名是否已经被注册
     * @param userName
     * @return
     */
    private boolean userNameIsRegister(String userName){

        String cacheKey = CacheKey.USER_LOGIN_PREFIX + "_" + userName;
        String cacheUser = redisService.getByKey(cacheKey);
        if(StringUtils.isNotEmpty(cacheUser)){
            logger.info("缓存命中 用户名已经存在: {}", userName);
            return true;
        }
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andUserNameEqualTo(userName);
        List<UserInfo> userList = userInfoMapper.selectByExample(userInfoExample);
        if(CollectionUtils.isNotEmpty(userList)){
            logger.info("数据库查询命中 用户名已经存在: {}", userName);
            return true;
        }
        return false;
    }
}
