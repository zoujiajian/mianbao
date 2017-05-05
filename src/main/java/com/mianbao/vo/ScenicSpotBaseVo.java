package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by zoujiajian on 2017-4-29.
 */
@Getter
@Setter
@ToString
public class ScenicSpotBaseVo {

    private Integer id;

    private String scenicSpotName;

    private String scenicSpotInfo;

    private List<String> scenicSpotPicture;

}
