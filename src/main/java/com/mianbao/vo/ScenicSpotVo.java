package com.mianbao.vo;

import com.mianbao.common.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by zoujiajian on 2017-4-28.
 */
@Getter
@Setter
@ToString
public class ScenicSpotVo extends ScenicSpotBaseVo{

    private Page<DynamicSimpleVo> page;

    private Date scenicCreateTime;

    private String createUser;
}
