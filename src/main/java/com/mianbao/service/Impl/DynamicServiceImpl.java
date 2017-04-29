package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.dao.*;
import com.mianbao.domain.*;
import com.mianbao.enums.Response;
import com.mianbao.exception.BusinessException;
import com.mianbao.facade.UserInfoFacade;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.pojo.user.UserSimpleInfo;
import com.mianbao.service.DynamicEvaluateService;
import com.mianbao.service.DynamicService;
import com.mianbao.service.FileLoadService;
import com.mianbao.service.RedisService;
import com.mianbao.util.BeanUtil;
import com.mianbao.util.CookieUtil;
import com.mianbao.util.PictureUtil;
import com.mianbao.vo.DynamicInfoAndReplyVo;
import com.mianbao.vo.DynamicInfoVo;
import com.mianbao.vo.DynamicLikeVo;
import com.mianbao.vo.DynamicReleaseVo;
import com.mianbao.worker.TopWorker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zoujiajian on 2017-4-8.
 */
@Service
public class DynamicServiceImpl implements DynamicService{

    private static final Logger logger = LoggerFactory.getLogger(DynamicServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private UserDynamicMapper userDynamicMapper;

    @Resource
    private TopWorker topWorker;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ScenicSpotDynamicMapper scenicSpotDynamicMapper;

    @Resource
    private ScenicSpotMapper scenicSpotMapper;

    @Resource
    private DynamicEvaluateService dynamicEvaluateService;

    @Resource
    private FileLoadService fileLoadService;

    @Override
    public Result releaseDynamic(HttpServletRequest request){

        Cookie token = CookieUtil.getTokenCookie(request);
        if(token == null){
            return Result.getDefaultError(Response.USER_NOT_LOGIN.getMsg());
        }

        String tokenKey = CacheKey.USER_TOKEN + "_" + token.getValue();
        String cacheValue = redisService.getByKey(tokenKey);
        if(StringUtils.isEmpty(cacheValue)){
            Result.getDefaultError(Response.USER_LOGIN_TIMEOUT.getMsg());
        }
        UserLogin userLogin = JSON.parseObject(cacheValue,UserLogin.class);

        String pictureAddress = fileLoadService.uploadFile(request);
        //如果图片存在 而且图片并没有上传成功
        if(StringUtils.isEmpty(pictureAddress) && fileLoadService.isContainsFile(request)){
            Result.getDefaultError(Response.RELEASE_DYNAMIC_FAIL.getMsg());
        }

        DynamicReleaseVo dynamicReleaseVo = BeanUtil.requestParse(request,DynamicReleaseVo.class);

        UserDynamic userDynamic = new UserDynamic();
        userDynamic.setUserId(userLogin.getId());
        userDynamic.setDynamicPicture(pictureAddress);
        userDynamic.setDynamicTitle(dynamicReleaseVo.getDynamicTitle());
        userDynamic.setDynamicContent(dynamicReleaseVo.getDynamicContent());
        userDynamic.setScenicSpotIds(dynamicReleaseVo.getScenicIds());

        return releaseDynamic(userDynamic);
    }


    @Transactional(rollbackFor = RuntimeException.class)
    private Result releaseDynamic(UserDynamic dynamic) {

        if(dynamic == null || dynamic.getUserId() == null){
            throw new BusinessException(Response.RELEASE_DYNAMIC_FAIL.getMsg(),Response.RELEASE_DYNAMIC_FAIL.getCode());
        }
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(dynamic.getUserId());
        if(userInfo == null){
            return Result.getDefaultError(Response.USER_NOT_CONTAINS.getMsg());
        }
        int record = userDynamicMapper.insertSelective(dynamic);
        if(record == 0){
            throw new RuntimeException();
        }

        List<ScenicSpotDynamic> scenicSpotDynamicList = Lists.newArrayList();
        String[] scenicIds = dynamic.getScenicSpotIds().split(",");
        for(String scenicId : scenicIds){
            //级联景点表中保存动态信息
            ScenicSpotDynamic scenicSpotDynamic = new ScenicSpotDynamic();
            scenicSpotDynamic.setDynamicId(dynamic.getId());
            scenicSpotDynamic.setScenicSpotId(Integer.valueOf(scenicId));

            scenicSpotDynamicList.add(scenicSpotDynamic);
        }

        int recordScenic = scenicSpotDynamicMapper.insertScenicDynamicList(scenicSpotDynamicList);
        if(recordScenic == 0){
            logger.error("recordScenic save error");
            throw new RuntimeException();
        }
        String cacheKey = CacheKey.USER_DYNAMIC_INFO_PREFIX + "_" + dynamic.getUserId() + "_" + dynamic.getId();
        redisService.addByKeyWithExpire(cacheKey, JSON.toJSONString(dynamic),CacheKey.DEFAULT_EXPIRE);

        return Result.getDefaultSuccess(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result getDynamicInfo(int dynamicId) {
        if(dynamicId < 0){
            throw new BusinessException(Response.DYNAMIC_NOT_CONTAINS.getMsg(),Response.DYNAMIC_NOT_CONTAINS.getCode());
        }

        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(dynamicId);
        if(userDynamic == null){
            return Result.getDefaultError(Response.DYNAMIC_NOT_CONTAINS.getMsg());
        }

        DynamicInfoVo dynamicInfoVo = getDynamicInfo(userDynamic);

        if(CollectionUtils.isEmpty(dynamicInfoVo.getScenicSpotName())){
            ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
            scenicSpotExample.createCriteria().andIdIn(dynamicInfoVo.getScenicSpotIds());

            List<ScenicSpot> scenicSpot = scenicSpotMapper.selectByExample(scenicSpotExample);

            for(ScenicSpot scenic : scenicSpot){
//                dynamicInfoVo.setScenicSpotName(scenic.getScenicSpotName());

                String scenicSpotKey = CacheKey.SCENIC_SPOT_INFO_PREFIX + "_" + scenic.getId();
                redisService.addByKey(scenicSpotKey, JSON.toJSONString(scenicSpotKey));
            }
        }

        Result result = dynamicEvaluateService.getDynamicEvaluateInfo(dynamicId);
        List<DynamicInfoAndReplyVo.Evaluate> evaluateList = Lists.newArrayList();
        if(result.isSuccess() && result.getData() != null){
            evaluateList = (List<DynamicInfoAndReplyVo.Evaluate>)result.getData();
        }

        DynamicInfoAndReplyVo dynamicInfoAndReplyVo = new DynamicInfoAndReplyVo();
        dynamicInfoAndReplyVo.setDynamicInfoVo(dynamicInfoVo);
        dynamicInfoAndReplyVo.setEvaluate(evaluateList);

        return Result.getDefaultSuccess(dynamicInfoAndReplyVo);
    }

    @Override
    public Result getUserAllDynamic(int userId) {
        if (userId < 0) {
            throw new BusinessException(Response.USER_NOT_CONTAINS.getMsg(), Response.USER_NOT_CONTAINS.getCode());
        }

        UserDynamicExample userDynamicExample = new UserDynamicExample();
        userDynamicExample.createCriteria().andUserIdEqualTo(userId);
        List<UserDynamic> userDynamicList = userDynamicMapper.selectByExample(userDynamicExample);
        List<DynamicInfoVo> resultDynamicList = Lists.newArrayList();

        for (UserDynamic userDynamic : userDynamicList) {
            resultDynamicList.add(getDynamicInfo(userDynamic));
        }

        return Result.getDefaultSuccess(resultDynamicList);
    }

    private DynamicInfoVo getDynamicInfo(UserDynamic userDynamic){

        String scenicIds = userDynamic.getScenicSpotIds();
        String[] array = scenicIds.split(",");
        List<Integer> ids = Lists.newArrayList();
        for(String id : array){
            ids.add(Integer.valueOf(id));
        }

        DynamicInfoVo dynamicInfoVo = new DynamicInfoVo();
        ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
        scenicSpotExample.createCriteria().andIdIn(ids);
        List<ScenicSpot> scenicSpots = scenicSpotMapper.selectByExample(scenicSpotExample);
        List<String> scenicSpotNames = Lists.newArrayList();
        for(ScenicSpot scenicSpot : scenicSpots){
            scenicSpotNames.add(scenicSpot.getScenicSpotName());
        }
        List<String> picture = Lists.newArrayList();
        String[] pictureAddress = userDynamic.getDynamicPicture().split(",");
        for(String address : pictureAddress){
            picture.add(PictureUtil.addHttpHost(address));
        }
        BeanUtils.copyProperties(userDynamic,dynamicInfoVo);
        dynamicInfoVo.setDynamicPicture(picture);
        dynamicInfoVo.setScenicSpotIds(ids);
        dynamicInfoVo.setScenicSpotName(scenicSpotNames);

        DynamicLikeVo dynamicLikeVo = getDynamicLikeInfo(userDynamic.getId());
        dynamicInfoVo.setDynamicLikeVo(dynamicLikeVo);
        return dynamicInfoVo;
    }


    /**
     * 获取点赞信息
     * @param dynamicId
     * @return
     */
    private DynamicLikeVo getDynamicLikeInfo(Integer dynamicId){
        String cacheKey = CacheKey.USER_DYNAMIC_LIKE_LIST + "_" + dynamicId;
        Set<String> cacheValue = redisService.getSetByKey(cacheKey);
        List<UserSimpleInfo> likeList = Lists.newArrayList();
        for(String value : cacheValue){
            likeList.add(JSON.parseObject(value,UserSimpleInfo.class));
        }

        DynamicLikeVo dynamicLikeVo = new DynamicLikeVo();
        dynamicLikeVo.setTotalCount(likeList.size());
        dynamicLikeVo.setLikeUser(likeList);

        return dynamicLikeVo;
    }

    /**
     * 动态点赞只使用缓存 不存数据库 保存一天的点赞用户
     * @param userId
     * @return
     */
    @Override
    public Result dynamicLike(int userId,int dynamicId) {
        if(userId < 0){
            throw new BusinessException(Response.USER_NOT_CONTAINS.getMsg(),Response.USER_NOT_CONTAINS.getCode());
        }
        if(dynamicId < 0){
            throw new BusinessException(Response.DYNAMIC_NOT_CONTAINS.getMsg(),Response.DYNAMIC_NOT_CONTAINS.getCode());
        }
        String cacheKey = CacheKey.USER_DYNAMIC_LIKE_LIST + "_" + dynamicId;
        String userInfoKey = CacheKey.USER_INFO_PREFIX + "_" + userId;
        String userInfo = redisService.getByKey(userInfoKey);
        UserInfo info;
        //检查缓存
        if(StringUtils.isNotEmpty(userInfo)){
            info = JSON.parseObject(userInfo,UserInfo.class);
        }else{
            info = userInfoMapper.selectByPrimaryKey(userId);
            redisService.addByKeyWithExpire(userInfoKey,JSON.toJSONString(userInfo),CacheKey.DEFAULT_EXPIRE);
        }
        //集合中存放用户id和用户名
        UserSimpleInfo simpleInfo = UserInfoFacade.userInfoToUserSimpleInfo(info);
        boolean addSet = redisService.addValueToSet(cacheKey,JSON.toJSONString(simpleInfo));
        if(addSet){
            //根据点赞量 实时更新排行榜
            topWorker.recordAccess(String.valueOf(dynamicId));
            return Result.getDefaultSuccess(null);
        }
        return Result.getDefaultError(Response.LIKE_DYNAMIC_FAIL.getMsg());
    }

    @Override
    public Result getDynamicLikeInfoList(int dynamicId) {
        if(dynamicId < 0){
            throw new BusinessException(Response.DYNAMIC_NOT_CONTAINS.getMsg(),Response.DYNAMIC_NOT_CONTAINS.getCode());
        }
        String cacheKey = CacheKey.USER_DYNAMIC_LIKE_LIST + "_" + dynamicId;
        Set<String> likeSet = redisService.getSetByKey(cacheKey);
        DynamicLikeVo dynamicLikeVo = new DynamicLikeVo();
        List<UserSimpleInfo> likeUserList = Lists.newArrayList();

        for(String value : likeSet){
            UserSimpleInfo simpleInfo = JSON.parseObject(value,UserSimpleInfo.class);
            likeUserList.add(simpleInfo);
        }
        dynamicLikeVo.setLikeUser(likeUserList);
        dynamicLikeVo.setTotalCount(likeUserList.size());

        return Result.getDefaultSuccess(dynamicLikeVo);
    }
}
