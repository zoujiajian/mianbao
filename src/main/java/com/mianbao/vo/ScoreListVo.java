package com.mianbao.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * Created by zoujiajian on 2017-5-18.
 */
@Getter
@Setter
@ToString
public class ScoreListVo {

    private double score;

    private String user;

    private String content;

    private String date;
}
