package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by zoujiajian on 2017-4-9.
 * 动态基本信息和评价
 */
@Getter
@Setter
@ToString
public class DynamicInfoAndReplyVo{

    //评价
    private List<Evaluate> evaluate;

    //动态基本信息
    private DynamicInfoVo dynamicInfoVo;

    @Getter
    @Setter
    @ToString
    public static class Evaluate{

        //评价内容
        private String evaluateContent;

        //评价人
        private String evaluateUser;

        //评价时间
        private Date date;

        //评价的回复
        private List<Reply> reply;
    }


    @Getter
    @Setter
    @ToString
    public static class Reply{

        //回复内容
        private String replyContent;

        //回复人
        private String replyUserName;

        //回复目标用户
        private String replyToUser;

        //回复时间
        private Date dateTime;
    }
}
