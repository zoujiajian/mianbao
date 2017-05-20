package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Page;
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
import com.mianbao.util.BadWordsUtil;
import com.mianbao.util.BeanUtil;
import com.mianbao.util.CookieUtil;
import com.mianbao.util.PictureUtil;
import com.mianbao.vo.*;
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
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private DynamicEvaluateMapper dynamicEvaluateMapper;

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

        DynamicReleaseVo dynamicReleaseVo = BeanUtil.requestParse(request,DynamicReleaseVo.class);
        //敏感词检测
        if(BadWordsUtil.searchBanWords(dynamicReleaseVo.getDynamicTitle()) || BadWordsUtil.searchBanWords(dynamicReleaseVo.getDynamicContent())){
            return Result.getDefaultError(Response.BAD_WORD.getMsg());
        }

        String pictureAddress = fileLoadService.uploadFile(request);
        //如果图片存在 而且图片并没有上传成功
        if(StringUtils.isEmpty(pictureAddress) && fileLoadService.isContainsFile(request)){
            Result.getDefaultError(Response.RELEASE_DYNAMIC_FAIL.getMsg());
        }

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

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userDynamic.getUserId());
        DynamicInfoVo dynamicInfoVo = getDynamicInfo(userDynamic);
        Result result = dynamicEvaluateService.getDynamicEvaluateInfo(dynamicId);
        List<DynamicInfoAndReplyVo.Evaluate> evaluateList = Lists.newArrayList();
        if(result.isSuccess() && result.getData() != null){
            evaluateList = (List<DynamicInfoAndReplyVo.Evaluate>)result.getData();
        }

        DynamicInfoAndReplyVo dynamicInfoAndReplyVo = new DynamicInfoAndReplyVo();
        dynamicInfoAndReplyVo.setDynamicInfoVo(dynamicInfoVo);
        dynamicInfoAndReplyVo.setEvaluate(evaluateList);
        dynamicInfoAndReplyVo.setUserId(userInfo.getId());
        dynamicInfoAndReplyVo.setUserName(userInfo.getUserName());
        dynamicInfoAndReplyVo.setDynamicId(dynamicId);
        dynamicInfoAndReplyVo.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(userDynamic.getCreatetime()));

        return Result.getDefaultSuccess(dynamicInfoAndReplyVo);
    }

    @Override
    public Result getUserAllDynamic(int userId, int pageNo ,int pageSize) {
        if (userId < 0 || pageNo < 0 || pageSize < 0) {
            throw new BusinessException(Response.USER_NOT_CONTAINS.getMsg(), Response.USER_NOT_CONTAINS.getCode());
        }

        Page<DynamicSimpleVo> page = new Page<>();
        UserDynamicExample userDynamicExample = new UserDynamicExample();
        userDynamicExample.createCriteria().andUserIdEqualTo(userId);

        long count = userDynamicMapper.countByExample(userDynamicExample);
        page.setRecords(count);
        page.setPage(pageNo);
        page.setPageSize(pageSize);
        List<UserDynamic> userDynamicList = userDynamicMapper.selectDynamicLimit(userId,page.getStartRecord(),page.getPageSize());
        List<DynamicSimpleVo> resultDynamicList = Lists.newArrayList();

        for (UserDynamic userDynamic : userDynamicList) {
            resultDynamicList.add(transSimpleVo(getDynamicInfo(userDynamic)));
        }

        page.setRows(resultDynamicList);

        return Result.getDefaultSuccess(page);
    }

    public static DynamicSimpleVo transSimpleVo(DynamicInfoVo dynamicInfoVo){

        DynamicSimpleVo dynamicSimpleVo = new DynamicSimpleVo();
        dynamicSimpleVo.setId(dynamicInfoVo.getId());
        dynamicSimpleVo.setDynamicTitle(dynamicInfoVo.getDynamicTitle());
        dynamicSimpleVo.setDynamicContent(dynamicInfoVo.getDynamicContent());
        dynamicSimpleVo.setDynamicPicture(dynamicInfoVo.getDynamicPicture().get(0));

        return dynamicSimpleVo;
    }


    public DynamicInfoVo getDynamicInfo(UserDynamic userDynamic){

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

    @Override
    public Result commentDynamic(int userId, int id, String content) {

        //敏感词检测
        if(BadWordsUtil.searchBanWords(content)){
            return Result.getDefaultError(Response.BAD_WORD.getMsg());
        }

        DynamicEvaluate dynamicEvaluate = new DynamicEvaluate();
        dynamicEvaluate.setDynamicId(id);
        dynamicEvaluate.setEvaluateUser(userId);
        dynamicEvaluate.setEvaluateContent(content);

        int record = dynamicEvaluateMapper.insert(dynamicEvaluate);
        if(record > 0){
            return Result.getDefaultSuccess(null);
        }

        return Result.getDefaultError(Response.COMMENT_DYNAMIC_FAIL.getMsg());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result deleteDynamic(int id) {
        if(id < 0){
            return Result.getDefaultError(Response.DYNAMIC_NOT_CONTAINS.getMsg());
        }
        int record = userDynamicMapper.deleteByPrimaryKey(id);
        if(record <= 0){
            throw new BusinessException();
        }
        //级联删除景点下的游记映射关系
        ScenicSpotDynamicExample scenicSpotDynamicExample = new ScenicSpotDynamicExample();
        scenicSpotDynamicExample.createCriteria().andDynamicIdEqualTo(id);
        record = scenicSpotDynamicMapper.deleteByExample(scenicSpotDynamicExample);
        if(record <= 0){
            throw new BusinessException();
        }

        //级联删除游记评价信息
        DynamicEvaluateExample dynamicEvaluateExample = new DynamicEvaluateExample();
        dynamicEvaluateExample.createCriteria().andDynamicIdEqualTo(id);
        dynamicEvaluateMapper.deleteByExample(dynamicEvaluateExample);

        return Result.getDefaultSuccess(null);
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

    @Override
    public Result getAllDynamicSimpleInfo(int pageNo, int pageSize) {

        if(pageNo < 0 || pageSize < 0){
            throw new BusinessException(Response.USER_NOT_CONTAINS.getMsg(), Response.USER_NOT_CONTAINS.getCode());
        }

        Page<DynamicSimpleVo> page = new Page<>();

        long count = userDynamicMapper.selectAllDynamicCount();
        page.setRecords(count);
        page.setPage(pageNo);
        page.setPageSize(pageSize);

        List<UserDynamic> userDynamicList = userDynamicMapper.selectAllDynamicLimit(page.getStartRecord(),page.getPageSize());
        List<DynamicSimpleVo> resultDynamicList = Lists.newArrayList();

        for (UserDynamic userDynamic : userDynamicList) {
            resultDynamicList.add(transSimpleVo(getDynamicInfo(userDynamic)));
        }

        page.setRows(resultDynamicList);

        return Result.getDefaultSuccess(page);
    }

    @Override
    public Result indexSimpleInfo() {

        List<UserDynamic> userDynamicList = userDynamicMapper.selectAllDynamicLimit(0,3);

        List<DynamicSimpleVo> resultDynamicList = Lists.newArrayList();

        for (UserDynamic userDynamic : userDynamicList) {
            resultDynamicList.add(transSimpleVo(getDynamicInfo(userDynamic)));
        }

        return Result.getDefaultSuccess(resultDynamicList);
    }
}
