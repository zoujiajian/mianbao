package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-5-5.
 */
@Getter
@Setter
@ToString
public class DynamicSimpleVo {

    private Integer id;

    //动态标题
    private String dynamicTitle;

    //动态内容
    private String dynamicContent;

    //动态图片
    private String dynamicPicture;
}
