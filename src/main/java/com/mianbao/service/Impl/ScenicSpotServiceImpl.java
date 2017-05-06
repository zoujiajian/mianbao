package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Page;
import com.mianbao.common.Result;
import com.mianbao.dao.*;
import com.mianbao.domain.*;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.*;
import com.mianbao.util.BeanUtil;
import com.mianbao.util.PictureUtil;
import com.mianbao.vo.DynamicSimpleVo;
import com.mianbao.vo.ScenicSpotBaseVo;
import com.mianbao.vo.ScenicSpotSimpleVo;
import com.mianbao.vo.ScenicSpotVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by zoujiajian on 2017-4-28.
 */
@Service
public class ScenicSpotServiceImpl implements ScenicSpotService{

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotServiceImpl.class);

    @Resource
    private DynamicService dynamicService;

    @Resource
    private ScenicSpotMapper scenicSpotMapper;

    @Resource
    private ScenicSpotDynamicMapper dynamicMapper;

    @Resource
    private UserDynamicMapper userDynamicMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserLikeMapper userLikeMapper;

    @Resource
    private FileLoadService fileLoadService;

    @Resource
    private RedisService redisService;

    @Override
    public Result addScenicSpot(HttpServletRequest request, String token) {

        String tokenKey = CacheKey.USER_TOKEN + "_" + token;
        String cacheValue = redisService.getByKey(tokenKey);
        if(StringUtils.isEmpty(cacheValue)){
            Result.getDefaultError(Response.USER_LOGIN_TIMEOUT.getMsg());
        }

        String pictureAddress = fileLoadService.uploadFile(request);
        if(StringUtils.isEmpty(pictureAddress) && fileLoadService.isContainsFile(request)){
            return Result.getDefaultError(Response.UPLOAD_PICTURE_FAIL.getMsg());
        }

        ScenicSpotBaseVo scenicSpotVo = BeanUtil.requestParse(request, ScenicSpotBaseVo.class);
        UserLogin userLogin = JSON.parseObject(cacheValue,UserLogin.class);

        ScenicSpot scenicSpot = new ScenicSpot();
        scenicSpot.setCreateUser(userLogin.getId());
        scenicSpot.setScenicSpotInfo(scenicSpotVo.getScenicSpotInfo());
        scenicSpot.setScenicSpotName(scenicSpotVo.getScenicSpotName());
        scenicSpot.setScenicSpotPicutre(pictureAddress);

        int record = scenicSpotMapper.insert(scenicSpot);
        if(record == 0){
            return Result.getDefaultError(Response.ADD_SCENIC_SPOT_FAIL.getMsg());
        }
        return Result.getDefaultSuccess(null);
    }

    @Override
    public Result getAllScenicSpot() {
        List<ScenicSpot> scenicSpotList = scenicSpotMapper.getAllScenicSpot();
        if(CollectionUtils.isEmpty(scenicSpotList)){
            return Result.getDefaultSuccess(null);
        }
        List<ScenicSpotBaseVo> baseVoList = Lists.newArrayList();
        for(ScenicSpot scenicSpot : scenicSpotList){
            ScenicSpotBaseVo baseVo = new ScenicSpotBaseVo();
            BeanUtils.copyProperties(scenicSpot,baseVo);

            baseVoList.add(baseVo);
        }

        return Result.getDefaultSuccess(baseVoList);
    }

    @Override
    public Result findByName(String scenicSpotName) {
        if(StringUtils.isEmpty(scenicSpotName)){
            return Result.getDefaultError(Response.SCENIC_SPOT_NOT_CONTAINS.getMsg());
        }

        ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
        String likeScenicSpotName = "%" + scenicSpotName + "%";
        scenicSpotExample.createCriteria().andScenicSpotNameLike(likeScenicSpotName);

        List<ScenicSpot> scenicSpotList = scenicSpotMapper.selectByExample(scenicSpotExample);
        if(CollectionUtils.isEmpty(scenicSpotList)){
            return Result.getDefaultError(Response.SCENIC_SPOT_NOT_CONTAINS.getMsg());
        }

        List<ScenicSpotBaseVo> baseVoLists = Lists.newArrayList();
        for(ScenicSpot scenicSpot : scenicSpotList){
            ScenicSpotBaseVo baseVo = new ScenicSpotBaseVo();
            BeanUtils.copyProperties(scenicSpot,baseVo);

            baseVoLists.add(baseVo);
        }

        return Result.getDefaultSuccess(baseVoLists);
    }

    @Override
    public Result indexScenicSpot() {

        List<ScenicSpot> scenicSpotList = scenicSpotMapper.selectTopScenicSpot(0,3);
        List<ScenicSpotSimpleVo> spotSimpleVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(scenicSpotList)){
            for(ScenicSpot scenicSpot : scenicSpotList){
               ScenicSpotSimpleVo scenicSpotSimpleVo = new ScenicSpotSimpleVo();

               BeanUtils.copyProperties(scenicSpot,scenicSpotSimpleVo);
               scenicSpotSimpleVo.setScenicSpotPicture(getScenicSpotAllPicture(scenicSpot).get(0));
               spotSimpleVoList.add(scenicSpotSimpleVo);
            }
        }
        return Result.getDefaultSuccess(spotSimpleVoList);
    }

    @Override
    public Result getScenicSpotInfoWithDynamic(int scenicId,int pageNo,int pageSize) {

        if(scenicId < 0){
            return Result.getDefaultError(Response.SCENIC_SPOT_NOT_CONTAINS.getMsg());
        }
        ScenicSpot scenicSpot = scenicSpotMapper.selectByPrimaryKey(scenicId);
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(scenicSpot.getCreateUser());

        ScenicSpotDynamicExample dynamicExample = new ScenicSpotDynamicExample();
        dynamicExample.createCriteria().andScenicSpotIdEqualTo(scenicId);
        long count = dynamicMapper.countByExample(dynamicExample);

        Page<DynamicSimpleVo> page = new Page<>();
        page.setPage(pageNo);
        page.setPageSize(pageSize);
        page.setRecords(count);

        List<ScenicSpotDynamic> scenicSpotDynamicList = dynamicMapper.selectWithLimit(scenicId,page.getStartRecord(),page.getPageSize());
        List<Integer> dynamicIds = Lists.newArrayList();
        for(ScenicSpotDynamic dynamic :scenicSpotDynamicList){
            dynamicIds.add(dynamic.getDynamicId());
        }
        UserDynamicExample userDynamicExample = new UserDynamicExample();
        userDynamicExample.createCriteria().andIdIn(dynamicIds);
        List<UserDynamic> userDynamicList = userDynamicMapper.selectByExample(userDynamicExample);

        List<DynamicSimpleVo> resultDynamicList = Lists.newArrayList();
        for(UserDynamic userDynamic : userDynamicList){
            resultDynamicList.add(DynamicServiceImpl.transSimpleVo(dynamicService.getDynamicInfo(userDynamic)));
        }
        page.setRows(resultDynamicList);

        List<String> address = getScenicSpotAllPicture(scenicSpot);
        ScenicSpotVo scenicSpotVo = new ScenicSpotVo();
        scenicSpotVo.setId(scenicSpot.getId());
        scenicSpotVo.setScenicSpotName(scenicSpot.getScenicSpotName());
        scenicSpotVo.setScenicSpotInfo(scenicSpot.getScenicSpotInfo());
        scenicSpotVo.setScenicCreateTime(scenicSpot.getScenicCreatetime());
        scenicSpotVo.setScenicSpotPicture(address);
        scenicSpotVo.setCreateUser(userInfo.getUserName());
        scenicSpotVo.setPage(page);

        return Result.getDefaultSuccess(scenicSpotVo);
    }

    @Override
    public Result collectionScenicSpot(int scenicId, int userId) {
        if(scenicId < 0 || userId < 0){
            return Result.getDefaultError(Response.SCENIC_SPOT_COLLECTION.getMsg());
        }

        UserLikeExample userLikeExample = new UserLikeExample();
        userLikeExample.createCriteria().andScenicSpotIdEqualTo(scenicId).andUserIdEqualTo(userId);
        List<UserLike> userLikes = userLikeMapper.selectByExample(userLikeExample);
        if(CollectionUtils.isNotEmpty(userLikes)){
            return Result.getDefaultError(Response.SCENIC_SPOT_CONTAINS_COLLECTION.getMsg());
        }

        UserLike userLike = new UserLike();
        userLike.setScenicSpotId(scenicId);
        userLike.setUserId(userId);
        int record = userLikeMapper.insert(userLike);
        if(record > 0){
            return Result.getDefaultSuccess(null);
        }

        return Result.getDefaultError(Response.SCENIC_SPOT_COLLECTION.getMsg());
    }

    @Override
    public Result collectionWithLimit(int userId, int pageNo, int pageSize) {
        if(userId < 0|| pageNo <0 || pageSize < 0){
            return Result.getDefaultError(Response.SELECT_SCENIC_SPOT_LIST_FAIL.getMsg());
        }

        UserLikeExample userLikeExample = new UserLikeExample();
        userLikeExample.createCriteria().andUserIdEqualTo(userId);
        long count = userLikeMapper.countByExample(userLikeExample);
        if(count <= 0){
            return Result.getDefaultSuccess(null);
        }

        Page<ScenicSpotSimpleVo> page = new Page<>();
        page.setRecords(count);
        page.setPageSize(pageSize);
        page.setPage(pageNo);
        List<UserLike> userLikeList = userLikeMapper.selectScenicSpotWithLimit(userId,page.getStartRecord(),pageSize);
        List<Integer> scenicIds = Lists.newArrayList();
        for(UserLike userLike : userLikeList){
            scenicIds.add(userLike.getScenicSpotId());
        }

        ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
        scenicSpotExample.createCriteria().andIdIn(scenicIds);
        List<ScenicSpot>  scenicSpotList = scenicSpotMapper.selectByExample(scenicSpotExample);
        if(CollectionUtils.isEmpty(scenicSpotList)){
            return Result.getDefaultSuccess(null);
        }

        List<ScenicSpotSimpleVo> spotSimpleVoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(scenicSpotList)){
            for(ScenicSpot scenicSpot : scenicSpotList){
                ScenicSpotSimpleVo scenicSpotSimpleVo = new ScenicSpotSimpleVo();

                BeanUtils.copyProperties(scenicSpot,scenicSpotSimpleVo);
                scenicSpotSimpleVo.setScenicSpotPicture(getScenicSpotAllPicture(scenicSpot).get(0));
                spotSimpleVoList.add(scenicSpotSimpleVo);
            }
        }
        page.setRows(spotSimpleVoList);

        return Result.getDefaultSuccess(page);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Result collectionSearch(String condition, int userId, int pageNo, int pageSize) {
        if(userId < 0|| pageNo <0 || pageSize < 0 || StringUtils.isEmpty(condition)){
            return Result.getDefaultError(Response.SEARCH_SCENIC_SPOT_FAIL.getMsg());
        }

        Result result = findByName(condition);
        if(!result.isSuccess()){
            return Result.getDefaultError(result.getErrorMsg());
        }
        List<ScenicSpotBaseVo> list = (List<ScenicSpotBaseVo>)result.getData();
        List<Integer> scenicIds = Lists.newArrayList();
        for(ScenicSpotBaseVo scenicSpotBaseVo : list){
            scenicIds.add(scenicSpotBaseVo.getId());
        }

        UserLikeExample userLikeExample = new UserLikeExample();
        userLikeExample.createCriteria().andUserIdEqualTo(userId).andScenicSpotIdIn(scenicIds);
        long count = userLikeMapper.countByExample(userLikeExample);
        if(count <=0){
            return Result.getDefaultSuccess(null);
        }

        Page<ScenicSpotSimpleVo> page = new Page<>();
        page.setRecords(count);
        page.setPageSize(pageSize);
        page.setPage(pageNo);
        List<Integer> ids = Lists.newArrayList();
        List<UserLike> userLikeList = userLikeMapper.selectScenicSpotWithLimitAndCondi(scenicIds,userId,page.getStartRecord(),pageSize);
        if(CollectionUtils.isEmpty(userLikeList)){
            return Result.getDefaultSuccess(null);
        }

        for(UserLike userLike : userLikeList){
            ids.add(userLike.getScenicSpotId());
        }
        ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
        scenicSpotExample.createCriteria().andIdIn(ids);
        List<ScenicSpot>  scenicSpotList = scenicSpotMapper.selectByExample(scenicSpotExample);
        if(CollectionUtils.isEmpty(scenicSpotList)){
            return Result.getDefaultSuccess(null);
        }
        List<ScenicSpotSimpleVo> spotSimpleVoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(scenicSpotList)){
            for(ScenicSpot scenicSpot : scenicSpotList){
                ScenicSpotSimpleVo scenicSpotSimpleVo = new ScenicSpotSimpleVo();

                BeanUtils.copyProperties(scenicSpot,scenicSpotSimpleVo);
                scenicSpotSimpleVo.setScenicSpotPicture(getScenicSpotAllPicture(scenicSpot).get(0));
                spotSimpleVoList.add(scenicSpotSimpleVo);
            }
        }
        page.setRows(spotSimpleVoList);

        return Result.getDefaultSuccess(page);
    }

    @Override
    public Result revoke(int userId, int scenicId) {

        if(userId < 0 || scenicId <0){
            return Result.getDefaultError(Response.REVOKE_COLLECTION_FAIL.getMsg());
        }

        UserLikeExample userLikeExample = new UserLikeExample();
        userLikeExample.createCriteria().andUserIdEqualTo(userId).andScenicSpotIdEqualTo(scenicId);

        int record = userLikeMapper.deleteByExample(userLikeExample);
        if(record > 0){
            return Result.getDefaultSuccess(null);
        }

        return Result.getDefaultError(Response.REVOKE_COLLECTION_FAIL.getMsg());
    }

    @Override
    public Result selectAllScenicInfo(Map<String, Object> params) {

        if(params == null){
            return Result.getDefaultError(Response.SELECT_SCENIC_SPOT_INFO_FAIL.getMsg());
        }

        Page<ScenicSpotSimpleVo> page = new Page<>();
        page.setPage(Integer.valueOf(params.get("pageNo").toString()));
        page.setPageSize(Integer.valueOf(params.get("pageSize").toString()));

        long count;
        List<ScenicSpot> scenicSpotList;
        if(params.containsKey("condition")){

            String condition = params.get("condition").toString();
            ScenicSpotExample scenicSpotExample = new ScenicSpotExample();
            String likeScenicSpotName = "%" + condition + "%";
            scenicSpotExample.createCriteria().andScenicSpotNameLike(likeScenicSpotName);

            count = scenicSpotMapper.countByExample(scenicSpotExample);
            if(count == 0){
                return Result.getDefaultSuccess(null);
            }
            scenicSpotList = scenicSpotMapper.selectTopScenicSpotWithCondition(likeScenicSpotName,
                    page.getStartRecord(),page.getPageSize());

        }else{
            count = scenicSpotMapper.countAll();
            if(count == 0){
                return Result.getDefaultSuccess(null);
            }
            scenicSpotList = scenicSpotMapper.selectTopScenicSpot(page.getStartRecord(),page.getPageSize());
        }

        List<ScenicSpotSimpleVo> spotSimpleVoList = scenicSpotToScenicSimpleVo(scenicSpotList);
        page.setRows(spotSimpleVoList);
        page.setRecords(count);

        return Result.getDefaultSuccess(page);
    }


    private List<String> getScenicSpotAllPicture(ScenicSpot scenicSpot){
        List<String> picture = Lists.newArrayList();
        String[] pictureAddress = scenicSpot.getScenicSpotPicutre().split(",");
        for(String address : pictureAddress){
            picture.add(PictureUtil.addHttpHost(address));
        }
        return picture;
    }

    private List<ScenicSpotSimpleVo> scenicSpotToScenicSimpleVo(List<ScenicSpot> scenicSpotList){

        List<ScenicSpotSimpleVo> spotSimpleVoList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(scenicSpotList)){
            for(ScenicSpot scenicSpot : scenicSpotList){
                ScenicSpotSimpleVo scenicSpotSimpleVo = new ScenicSpotSimpleVo();

                BeanUtils.copyProperties(scenicSpot,scenicSpotSimpleVo);
                scenicSpotSimpleVo.setScenicSpotPicture(getScenicSpotAllPicture(scenicSpot).get(0));
                spotSimpleVoList.add(scenicSpotSimpleVo);
            }
        }

        return spotSimpleVoList;
    }
}
