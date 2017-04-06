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

    /**
     * 删除有序集合中 x 到 y 区间的所有元素
     * @param key
     * @param x
     * @param y
     * @return
     */
    long delOrderSetBetweenXAndY(String key, long x , long y);

    /**
     * 指定成员的分数加1
     * @param key
     * @param member
     * @return
     */
    double orderSetScoreIncrByMember(String key,String member);

    /**
     *
     * @param timeOut  等待时间
     * @param lockTime 锁定时间
     * @return
     */
    boolean lock(long timeOut, long lockTime) throws InterruptedException;

    /**
     *
     * @param timeOut 等待时间
     * @return
     */
    boolean lock(long timeOut) throws InterruptedException;

    /**
     * 尝试获取锁 不等待
     * @return
     */
    boolean tryLock() throws InterruptedException;

    /**
     * 释放锁
     */
    void unLock();

    /**
     * 判断当前key在缓存中是否存在
     * @param key
     * @return
     */
    boolean exists(String key);
}
