package com.mianbao.service;

/**
 * Created by zoujiajian on 2017-4-8.
 */
public interface RedisLockService {

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

}
