package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.dao.ScenicSpotDynamicMapper;
import com.mianbao.dao.ScenicSpotMapper;
import com.mianbao.dao.UserDynamicMapper;
import com.mianbao.dao.UserInfoMapper;
import com.mianbao.domain.*;
import com.mianbao.enums.Response;
import com.mianbao.exception.BusinessException;
import com.mianbao.facade.UserInfoFacade;
import com.mianbao.pojo.user.UserSimpleInfo;
import com.mianbao.service.DynamicService;
import com.mianbao.service.RedisService;
import com.mianbao.vo.DynamicInfoAndReplyVo;
import com.mianbao.vo.DynamicInfoVo;
import com.mianbao.vo.DynamicLikeVo;
import com.mianbao.worker.TopWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result releaseDynamic(UserDynamic dynamic) {
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
        //级联景点表中保存动态信息
        ScenicSpotDynamic scenicSpotDynamic = new ScenicSpotDynamic();
        scenicSpotDynamic.setDynamicId(dynamic.getId());
        scenicSpotDynamic.setScenicSpotId(dynamic.getScenicSpot());
        int recordScenic = scenicSpotDynamicMapper.insertSelective(scenicSpotDynamic);
        if(recordScenic == 0){
            throw new RuntimeException();
        }
        String cacheKey = CacheKey.USER_DYNAMIC_INFO_PREFIX + "_" + dynamic.getUserId() + "_" + dynamic.getId();
        redisService.addByKeyWithExpire(cacheKey, JSON.toJSONString(dynamic),CacheKey.DEFAULT_EXPIRE);

        return Result.getDefaultSuccess(null);
    }

    @Override
    public Result getDynamicInfo(int dynamicId) {
        if(dynamicId < 0){
            throw new BusinessException(Response.DYNAMIC_NOT_CONTAINS.getMsg(),Response.DYNAMIC_NOT_CONTAINS.getCode());
        }

        UserDynamic userDynamic = userDynamicMapper.selectByPrimaryKey(dynamicId);
        if(userDynamic == null){
            return Result.getDefaultError(Response.DYNAMIC_NOT_CONTAINS.getMsg());
        }
        DynamicInfoAndReplyVo dynamicInfoAndReplyVo = new DynamicInfoAndReplyVo();
        DynamicInfoVo dynamicInfoVo = getDynamicInfo(userDynamic);
        if(StringUtils.isEmpty(dynamicInfoVo.getScenicSpotName())){
            ScenicSpot scenicSpot = scenicSpotMapper.selectByPrimaryKey(userDynamic.getScenicSpot());
            dynamicInfoVo.setScenicSpotName(scenicSpot.getScenicSpotName());

            String scenicSpotKey = CacheKey.SCENIC_SPOT_INFO_PREFIX + "_" + scenicSpot.getId();
            redisService.addByKey(scenicSpotKey, JSON.toJSONString(scenicSpotKey));
        }

        dynamicInfoAndReplyVo.setDynamicInfoVo(dynamicInfoVo);
        return null;
    }

    @Override
    public Result getUserAllDynamic(int userId) {
        if(userId < 0){
            throw new BusinessException(Response.USER_NOT_CONTAINS.getMsg(),Response.USER_NOT_CONTAINS.getCode());
        }
        UserDynamicExample userDynamicExample = new UserDynamicExample();
        userDynamicExample.createCriteria().andUserIdEqualTo(userId);
        List<UserDynamic> userDynamicList = userDynamicMapper.selectByExample(userDynamicExample);
        List<DynamicInfoVo> resultDynamicList = Lists.newArrayList();
        List<Integer> scenicSpot = Lists.newArrayList();
        for(UserDynamic userDynamic : userDynamicList){
            DynamicInfoVo dynamicInfoVo = getDynamicInfo(userDynamic);
            if(StringUtils.isEmpty(dynamicInfoVo.getScenicSpotName())){
                scenicSpot.add(userDynamic.getScenicSpot());
            }

            resultDynamicList.add(dynamicInfoVo);
        }
        ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
        scenicSpotExample.createCriteria().andIdIn(scenicSpot);
        Map<Integer,String> temp = Maps.newHashMap();
        List<ScenicSpot> scenicSpots = scenicSpotMapper.selectByExample(scenicSpotExample);
        for(ScenicSpot spot : scenicSpots){
            temp.put(spot.getId(),spot.getScenicSpotName());
            String scenicSpotKey = CacheKey.SCENIC_SPOT_INFO_PREFIX + "_" + spot.getId();
            redisService.addByKey(scenicSpotKey, JSON.toJSONString(spot));
        }

        for(DynamicInfoVo dynamicInfoVo : resultDynamicList){
            if(StringUtils.isEmpty(dynamicInfoVo.getScenicSpotName())){
                String scenicSpotName = temp.get(dynamicInfoVo.getScenicSpotId());
                dynamicInfoVo.setScenicSpotName(scenicSpotName);
            }
        }
        return Result.getDefaultSuccess(resultDynamicList);
    }

    /**
     * 获取动态基本信息
     * @param
     * @param userDynamic
     * @return
     */
    private DynamicInfoVo getDynamicInfo(UserDynamic userDynamic){

        DynamicInfoVo dynamicInfoVo  = new DynamicInfoVo();
        String picture = userDynamic.getDynamicPicutre();
        DynamicLikeVo dynamicLikeVo = getDynamicLikeInfo(userDynamic.getId());

        dynamicInfoVo.setDynamicLikeVo(dynamicLikeVo);
        dynamicInfoVo.setCreateTime(userDynamic.getCreatetime());
        dynamicInfoVo.setDynamicContent(userDynamic.getDynamicContent());
        dynamicInfoVo.setDynamicPicture(StringUtils.isEmpty(picture)? null: Lists.newArrayList(picture.split(",")));
        dynamicInfoVo.setDynamicTitle(userDynamic.getDynamicTitle());
        dynamicInfoVo.setScenicSpotId(userDynamic.getScenicSpot());

        //查询缓存 是否缓存景点信息
        String scenicSpotKey = CacheKey.SCENIC_SPOT_INFO_PREFIX + "_" + userDynamic.getScenicSpot();
        String value = redisService.getByKey(scenicSpotKey);
        if(StringUtils.isNotEmpty(value)) {
            ScenicSpot spot = JSON.parseObject(value,ScenicSpot.class);
            dynamicInfoVo.setScenicSpotName(spot.getScenicSpotName());
        }

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
