package com.mianbao.service.Impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mianbao.common.CacheKey;
import com.mianbao.common.Result;
import com.mianbao.dao.DynamicEvaluateMapper;
import com.mianbao.dao.EvaluateReplyMapper;
import com.mianbao.dao.UserInfoMapper;
import com.mianbao.domain.*;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserSimpleInfo;
import com.mianbao.service.DynamicEvaluateService;
import com.mianbao.service.RedisService;
import com.mianbao.vo.DynamicInfoAndReplyVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zoujiajian on 2017-4-22.
 * 动态评价serviceImpl
 */
@Service
public class DynamicEvaluateServiceImpl implements DynamicEvaluateService{

    private static final Logger logger = LoggerFactory.getLogger(DynamicEvaluateServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private DynamicEvaluateMapper dynamicEvaluateMapper;

    @Resource
    private EvaluateReplyMapper evaluateReplyMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Result getDynamicEvaluateInfo(int dynamicId) {
        if(dynamicId <= 0){
            return Result.getDefaultError(Response.DYNAMIC_NOT_CONTAINS.getMsg());
        }
        String cacheKey = CacheKey.USER_DYNAMIC_LIKE_LIST + "_" + dynamicId;
        String cacheValue = redisService.getByKey(cacheKey);
        List<UserSimpleInfo> userSimpleInfoList = Lists.newArrayList();
        if(StringUtils.isNotEmpty(cacheValue)){
            userSimpleInfoList = JSON.parseArray(cacheValue,UserSimpleInfo.class);
        }
        DynamicEvaluateExample example = new DynamicEvaluateExample();
        example.createCriteria().andDynamicIdEqualTo(dynamicId);
        List<DynamicEvaluate> evaluateList = dynamicEvaluateMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(evaluateList)){
            return Result.getDefaultSuccess(null);
        }
        //获取评价的id
        List<Integer> evaluateId = Lists.newArrayList();
        for(DynamicEvaluate dynamicEvaluate : evaluateList){
            evaluateId.add(dynamicEvaluate.getId());
        }
        //获取评价下的所有回复
        EvaluateReplyExample evaluateReplyExample = new EvaluateReplyExample();
        evaluateReplyExample.createCriteria().andEvaluateIn(evaluateId);
        List<EvaluateReply> evaluateReplyList = evaluateReplyMapper.selectByExample(evaluateReplyExample);
        Map<Integer,String> userSimpleInfo = getUserSimpleInfo(evaluateReplyList, userSimpleInfoList);
        Map<Integer, List<DynamicInfoAndReplyVo.Reply>> evaluateGroup = groupByDynamic(evaluateReplyList,userSimpleInfo);
        List<DynamicInfoAndReplyVo.Evaluate> evaluates = dataTransfer(evaluateGroup,evaluateList,userSimpleInfo);

        return Result.getDefaultSuccess(evaluates);
    }


    /**
     * 按照评价id 分回复组
     * @param evaluateReplyList
     * @return
     */
    private Map<Integer, List<DynamicInfoAndReplyVo.Reply>> groupByDynamic(List<EvaluateReply> evaluateReplyList,Map<Integer,String> userSimpleInfo){
        if(CollectionUtils.isEmpty(evaluateReplyList) || userSimpleInfo.size() == 0){
            return Maps.newHashMap();
        }
        Map<Integer,String> userSimpleInfoMap = Maps.newHashMap();
        Map<Integer,List<DynamicInfoAndReplyVo.Reply>> replyMap = Maps.newHashMap();
        for(EvaluateReply evaluateReply : evaluateReplyList){

            DynamicInfoAndReplyVo.Reply reply = new DynamicInfoAndReplyVo.Reply();
            reply.setDateTime(evaluateReply.getCreateTime());
            reply.setReplyContent(evaluateReply.getReplyContent());
            reply.setReplyToUser(userSimpleInfoMap.get(evaluateReply.getToUserId()));
            reply.setReplyUserName(userSimpleInfoMap.get(evaluateReply.getReplyUserId()));

           if(replyMap.containsKey(evaluateReply.getEvaluate())){
                List<DynamicInfoAndReplyVo.Reply> list = replyMap.get(evaluateReply.getEvaluate());
                list.add(reply);
           }else{
                replyMap.put(evaluateReply.getEvaluate(),Lists.newArrayList(reply));
           }
        }

        //将评价的回复信息按时间排序
        for(Integer evaluate: replyMap.keySet()){
            replyMap.get(evaluate).sort(new Comparator<DynamicInfoAndReplyVo.Reply>() {
                @Override
                public int compare(DynamicInfoAndReplyVo.Reply o1, DynamicInfoAndReplyVo.Reply o2) {
                        return o1.getDateTime().compareTo(o2.getDateTime());
                    }
                }
            );
        }
        return replyMap;
    }

    /**
     * 获取用户简单信息
     * @param evaluateReplyList
     * @return
     */
    private Map<Integer,String> getUserSimpleInfo(List<EvaluateReply> evaluateReplyList, List<UserSimpleInfo> userSimpleInfoList){
        Map<Integer,String> userSimpleInfoMap = Maps.newHashMap();
        Set<Integer> userIds = Sets.newHashSet();
        for(EvaluateReply evaluateReply : evaluateReplyList){
            userIds.add(evaluateReply.getToUserId());
            userIds.add(evaluateReply.getReplyUserId());
        }
        // 移除缓存中已经存在的user
        for(UserSimpleInfo userSimpleInfo :userSimpleInfoList){
            Integer userId = userSimpleInfo.getId();
            userSimpleInfoMap.put(userId,userSimpleInfo.getUserName());
            if(userIds.contains(userId)){
                userIds.remove(userId);
            }
        }
        //TODO 是否需要设置动态关联的用户的简单信息缓存
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andIdIn(Lists.newArrayList(userIds));
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        for(UserInfo userInfo : userInfos){
            userSimpleInfoMap.put(userInfo.getId(),userInfo.getUserName());
        }

        return userSimpleInfoMap;
    }


    private List<DynamicInfoAndReplyVo.Evaluate> dataTransfer(Map<Integer,List<DynamicInfoAndReplyVo.Reply>> replyGroupInfo ,
                                                                       List<DynamicEvaluate> dynamicEvaluateList, Map<Integer,String> userSimpleInfo ){
        if(replyGroupInfo.size() == 0){
            throw new IllegalArgumentException();
        }
        List<DynamicInfoAndReplyVo.Evaluate> evaluates = Lists.newArrayList();
        for(DynamicEvaluate dynamicEvaluate : dynamicEvaluateList){

            DynamicInfoAndReplyVo.Evaluate evaluate = new DynamicInfoAndReplyVo.Evaluate();
            evaluate.setDate(dynamicEvaluate.getCreateTime());
            evaluate.setEvaluateContent(dynamicEvaluate.getEvaluateContent());
            evaluate.setEvaluateUser(userSimpleInfo.get(dynamicEvaluate.getEvaluateUser()));

            List<DynamicInfoAndReplyVo.Reply> replyList = replyGroupInfo.get(dynamicEvaluate.getId());
            evaluate.setReply(replyList);

            evaluates.add(evaluate);
        }

        return evaluates;
    }
}
