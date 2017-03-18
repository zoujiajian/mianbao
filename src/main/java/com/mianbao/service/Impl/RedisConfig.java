package com.mianbao.service.Impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zoujiajian on 2017-3-10.
 * redis 配置类
 */
@Component
public class RedisConfig{

    @Value("${redis.MaxActive}")
    private int maxActive;

    @Value("${redis.MaxIdle}")
    private int maxIdle;

    @Value("${redis.MaxWait}")
    private int maxWait;

    @Value("${redis.borrow}")
    private boolean borrow;

    @Value("${redis.address}")
    private String address;

    @Value("${redis.port}")
    private int port;

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public boolean isBorrow() {
        return borrow;
    }

    public void setBorrow(boolean borrow) {
        this.borrow = borrow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private JedisPool jedisPool;

    private JedisPool getJedisPool(){
        //双重check 实现单列（虽然这样也不能完全实现单列 先这样吧）
        if(jedisPool == null){
            synchronized (this){
                if(jedisPool == null){
                    return buildJedisPool();
                }
            }
        }
        return jedisPool;
    }

    private JedisPool buildJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxActive);
        config.setMaxIdle(maxIdle);
        config.setMaxWaitMillis(maxWait);
        config.setTestOnBorrow(borrow);
        jedisPool = new JedisPool(config,address,port);
        return jedisPool;
    }

    protected Jedis getJedisClient(){
        return getJedisPool().getResource();
    }

    protected void checkKey(String key){
        if(StringUtils.isEmpty(key)){
            throw new IllegalArgumentException("key is empty");
        }
    }

    protected void checkKv(String key, String value){
        if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
            throw new IllegalArgumentException("KV is empty");
        }
    }
}
