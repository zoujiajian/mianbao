package com.mianbao.common;

/**
 * Created by zoujiajian on 2017-4-6.
 * 缓存相关key公共类 便于之后查看缓存key
 */
public class CacheKey {

    /**
     * 项目中所有key的公共前缀
     */
    public static final String CACHE_PREFIX = "mianbao";

    public static final String USER_PICTURE_PREFIX = "user_picture_prefix";

    public static final String USER_LOGIN_PREFIX = "user_login_prefix";

    public static final String USER_INFO_PREFIX = "user_info_prefix";

    /**
     * 实时最热动态的key
     */
    public static final String TOP_DYNAMIC_INFO = "real_time_top";

    public static final int DEFAULT_EXPIRE = 1000 * 60 * 60 * 24;

    private CacheKey(){

    }
}