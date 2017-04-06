package com.mianbao.util;

/**
 * Created by zoujiajian on 2017-4-1.
 * 项目中所有key的前缀
 */
public class RedisKeyPrefixUtil {

    private static final String KEY_PREFIX = "c";

    public static String getKeyAddPrefix(String key){
        return KEY_PREFIX + key;
    }

    private RedisKeyPrefixUtil(){

    }
}
