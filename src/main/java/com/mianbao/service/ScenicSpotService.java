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

}
