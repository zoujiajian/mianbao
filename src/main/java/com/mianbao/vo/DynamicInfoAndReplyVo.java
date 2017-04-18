package com.mianbao.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-4-9.
 * 动态基本信息和评价
 */
@Getter
@Setter
@ToString
public class DynamicInfoAndReplyVo{

    //评价
    private Evaluate evaluate;

    //点赞信息
    private DynamicInfoVo dynamicInfoVo;

    @Getter
    @Setter
    @ToString
    private static class Evaluate{
        //评价内容
        private String evaluateContent;

        //评价人
        private String evaluateUser;

        //评价的回复
        private Reply reply;
    }


    @Getter
    @Setter
    @ToString
    private static class Reply{

        //回复内容
        private String replyContent;

        //回复人
        private String replyUserName;

        //回复目标用户
        private String replyToUser;

        //针对这条回复的下一个回复
        private Reply nextReply;
    }
}
