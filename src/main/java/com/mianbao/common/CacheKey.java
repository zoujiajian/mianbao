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

    /**
     * 用户图片信息前缀
     */
    public static final String USER_PICTURE_PREFIX = "user_picture_prefix";

    /**
     * 用户登录信息前缀
     */
    public static final String USER_LOGIN_PREFIX_NAME = "user_login_prefix_name";

    /**
     * 用户登录信息前缀
     */
    public static final String USER_LOGIN_PREFIX_ID = "user_login_prefix_id";

    /**
     * 用户详细信息前缀
     */
    public static final String USER_INFO_PREFIX = "user_info_prefix";

    /**
     * 用户动态信息前缀
     */
    public static final String USER_DYNAMIC_INFO_PREFIX = "user_dynamic_info_prefix";

    /**
     * 点赞动态的用户集合
     */
    public static final String USER_DYNAMIC_LIKE_LIST = "user_dynamic_like_list_prefix";

    /**
     * 实时最热动态的key
     */
    public static final String TOP_DYNAMIC_INFO = "real_time_top";

    /**
     * 景点信息的key
     */
    public static final String SCENIC_SPOT_INFO_PREFIX = "scenic_spot_info_prefix";

    /**
     * 用户token信息
     */
    public static final String USER_TOKEN  = "user_token";

    public static final int DEFAULT_EXPIRE = 1000 * 60 * 60 * 24;

    //服务端 token过期时间 单位毫秒
    public static final int SERVER_TOKEN_DEFAULT_EXPIRE = 1000 * 60 * 2;

    //客户端token过期时间 单位秒
    public static final int CLIENT_TOKEN_DEFUALT_EXPIRE = 365 * 24 * 60 * 60;

    private CacheKey(){

    }
}
