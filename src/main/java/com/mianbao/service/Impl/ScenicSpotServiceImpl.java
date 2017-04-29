package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.dao.ScenicSpotMapper;
import com.mianbao.domain.ScenicSpot;
import com.mianbao.domain.ScenicSpotExample;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.FileLoadService;
import com.mianbao.service.RedisService;
import com.mianbao.service.ScenicSpotService;
import com.mianbao.util.BeanUtil;
import com.mianbao.vo.ScenicSpotBaseVo;
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
/**
 * Created by zoujiajian on 2017-4-28.
 */
@Service
public class ScenicSpotServiceImpl implements ScenicSpotService{

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotServiceImpl.class);

    @Resource
    private ScenicSpotMapper scenicSpotMapper;

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

        ScenicSpotVo scenicSpotVo = BeanUtil.requestParse(request, ScenicSpotVo.class);
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
}
