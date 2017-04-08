package com.mianbao.pojo.dynamic;

import com.mianbao.pojo.BasePojo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zoujiajian on 2017-4-8.
 */
@Getter
@Setter
@ToString
public class DynamicLike extends BasePojo implements Serializable{

    /**
     * 总点赞量
     */
    private int totalCount;

    /**
     * 点赞用户名
     */
    private List<String> likeUser;

}
