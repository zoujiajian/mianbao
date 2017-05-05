package com.mianbao.service;

import com.mianbao.common.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoujiajian on 2017-4-28.
 * 景点service
 */
public interface ScenicSpotService {

    //添加景点信息
    Result addScenicSpot(HttpServletRequest request, String token);

    //获取所有的景点信息(景点的基本信息 不包含附加信息)
    Result getAllScenicSpot();

    //景点检索
    Result findByName(String scenicSpotName);

    //首页的展示信息
    Result indexScenicSpot();

    // 获取景点的详细信息 （包括景点评价 景点动态）
    Result getScenicSpotInfoWithDynamic(int scenicId,int pageNo,int pageSize);

    //景点收藏
    Result collectionScenicSpot(int scenicId, int userId);

    //获取用户收藏的景点
    Result collectionWithLimit(int userId, int pageNo, int pageSize);

    //搜索用户收藏的景点信息
    Result collectionSearch(String condition,int userId, int pageNo, int pageSize);

    //取消收藏
    Result revoke(int userId,int scenicId);
}
