package com.mianbao.service;

import java.util.Set;

/**
 * Created by zoujiajian on 2017-3-10.
 * redis 服务
 */
public interface RedisService {

    String getByKey(String key);

    boolean delByKey(String key);

    boolean addByKey(String key, String val);

    boolean addByKeyWithExpire(String key, String val, int expire);

    boolean updByKey(String key, String value);

    /**
     * redis 原子递增
     * @param key
     * @return
     */
    boolean incr(String key);

    /**
     * 添加score到有序列表
     * @param key
     * @param score
     * @param member
     * @return
     */
    boolean addToOrderSet(String key, int score, String member);

    /**
     * 从有序列表中取出前top个元素
     * @param key
     * @param top
     * @return
     */
    Set<String> getTopByOrderSet(String key, int top);

}
