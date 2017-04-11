package com.mianbao.pojo.user;

import com.mianbao.pojo.BasePojo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zoujiajian on 2017-4-9.
 */
@Getter
@Setter
public class UserSimpleInfo extends BasePojo implements Serializable{

    private int id;

    private String userName;
}
