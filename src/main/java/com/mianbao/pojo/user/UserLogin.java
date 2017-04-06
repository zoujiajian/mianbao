package com.mianbao.pojo.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-21.
 */
@Getter
@Setter
@ToString
public class UserLogin {

    private int id;

    private String userName;

    private String password;
}
