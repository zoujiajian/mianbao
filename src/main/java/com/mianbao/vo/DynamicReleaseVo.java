package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * Created by zoujiajian on 2017-4-26.
 */
@Getter
@Setter
@ToString
public class DynamicReleaseVo {

    //用户id
    private int userId;

    //动态标题
    private String dynamicTitle;

    //动态内容
    private String dynamicContent;

    //动态图片
    private List<String> dynamicPicture;

    //关联景点id
    private String scenicIds;

    //发布时间
    private Date createTime;

}
