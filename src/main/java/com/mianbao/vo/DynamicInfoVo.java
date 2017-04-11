package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-4-9.
 * 动态基本展示信息
 */
@Setter
@Getter
@ToString
public class DynamicInfoVo {

    //点赞信息
    private DynamicLikeVo dynamicLikeVo;

    //动态标题
    private String dynamicTitle;

    //动态内容
    private String dynamicContent;

    //动态图片
    private String dynamicPicture;

    //关联景点
    private String scenicSpot;

    //发布时间
    private String createTime;
}
