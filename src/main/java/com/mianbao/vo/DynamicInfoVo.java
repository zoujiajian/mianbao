package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by zoujiajian on 2017-4-9.
 * 动态基本展示信息
 */
@Setter
@Getter
@ToString
public class DynamicInfoVo {

    private Integer id;

    //点赞信息
    private DynamicLikeVo dynamicLikeVo;

    //动态标题
    private String dynamicTitle;

    //动态内容
    private String dynamicContent;

    //动态图片
    private List<String> dynamicPicture;

    //关联景点名称
    private List<String> scenicSpotName;

    //关联景点id
    private List<Integer> scenicSpotIds;

    //发布时间
    private Date createTime;
}
