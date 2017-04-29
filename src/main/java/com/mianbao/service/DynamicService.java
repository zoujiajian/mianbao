package com.mianbao.service;

import com.mianbao.common.Result;
import com.mianbao.domain.UserDynamic;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zoujiajian on 2017-4-8.
 */
public interface DynamicService {

    /**
     * 用户发布动态
     * @param request
     * @return
     */
    Result releaseDynamic(HttpServletRequest request);


    /**
     * 查看动态信息 包括评价 回复 点赞量 等
     * @param dynamicId
     * @return
     */
    Result getDynamicInfo(int dynamicId);

    /**
     * 获取用户所有的动态信息  只显示到点赞量 和点赞用户信息
     * @param userId
     * @return
     */
    Result getUserAllDynamic(int userId);

    /**
     * 点赞动态
     * @param userId
     * @param dynamicId
     * @return
     */
    Result dynamicLike(int userId,int dynamicId);


    /**
     * 获取动态的点赞信息 和点赞量
     * @param dynamicId
     * @return
     */
    Result getDynamicLikeInfoList(int dynamicId);
}
