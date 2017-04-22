package com.mianbao.service;

import com.mianbao.common.Result;

import java.util.List;

/**
 * Created by zoujiajian on 2017-4-22.
 * 动态评价service
 */
public interface DynamicEvaluateService {

    /**
     * 获取动态下的评价和回复信息（不包括动态本身的信息）
     * @param dynamicId
     * @return
     */
    Result getDynamicEvaluateInfo(int dynamicId);



}
