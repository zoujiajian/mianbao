package com.mianbao.vo;

import com.mianbao.pojo.BasePojo;
import com.mianbao.pojo.user.UserSimpleInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zoujiajian on 2017-4-8.
 * 点赞相关信息
 */
@Getter
@Setter
@ToString
public class DynamicLikeVo extends BasePojo implements Serializable{

    /**
     * 总点赞量
     */
    private int totalCount;

    /**
     * 点赞用户基本信息
     */
    private List<UserSimpleInfo> likeUser;

}
