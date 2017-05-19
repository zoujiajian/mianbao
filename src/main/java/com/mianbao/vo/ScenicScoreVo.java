package com.mianbao.vo;

import com.mianbao.common.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-5-18.
 */
@Getter
@Setter
@ToString
public class ScenicScoreVo {

    private double avgScore;

    private Page<ScoreListVo> page;

}
