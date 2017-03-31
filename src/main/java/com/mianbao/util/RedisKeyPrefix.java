package com.mianbao.util;

/**
 * Created by zoujiajian on 2017-4-1.
 * 项目中所有key的前缀
 */
public class RedisKeyPrefix {

    public static final String KEY_PREFIX = "mianbao_";

    public static String getKeyAddPrefix(String key){
        return KEY_PREFIX + key;
    }

    private RedisKeyPrefix(){

    }
}
