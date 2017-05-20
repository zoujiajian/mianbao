package com.mianbao.service;

import com.mianbao.common.Result;
import com.mianbao.domain.UserDynamic;
import com.mianbao.vo.DynamicInfoVo;

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
    Result getUserAllDynamic(int userId,int pageNo,int pageSize);

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


    /**
     * 分页需要展示的动态信息
     * @return
     */
    Result getAllDynamicSimpleInfo(int pageNo,int pageSize);

    /**
     * 获取首页展示的信息
     * @return
     */
    Result indexSimpleInfo();

    DynamicInfoVo getDynamicInfo(UserDynamic userDynamic);

    /**
     * 评价动态
     * @param userId
     * @param id
     * @param content
     * @return
     */
    Result commentDynamic(int userId, int id, String content);

    /**
     * 删除游记
     * @param id
     * @return
     */
    Result deleteDynamic(int id);
}
