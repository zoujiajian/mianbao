package com.mianbao.service;

import com.mianbao.common.Result;
import com.mianbao.domain.UserDynamic;


/**
 * Created by zoujiajian on 2017-4-8.
 */
public interface DynamicService {

    /**
     * 用户发布动态
     * @param dynamic
     * @return
     */
    Result releaseDynamic(UserDynamic dynamic);

    /**
     * 查看动态信息 包括评价 回复 点赞量 浏览量 等
     * @param dynamicId
     * @return
     */
    Result getDynamicInfo(int dynamicId);

    /**
     * 获取用户所有的动态信息  包括评价 回复 点赞量 浏览量 等
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
}
