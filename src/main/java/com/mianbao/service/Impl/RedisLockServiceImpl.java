package com.mianbao.service.Impl;

import com.mianbao.service.RedisLockService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;


/**
 * Created by zoujiajian on 2017-4-8.
 */
@Service
public class RedisLockServiceImpl extends RedisConfig implements RedisLockService {


    private static final String LOCK = "mianbao_redis_lock";

    private boolean acquire(long timeOut,long lockTime) throws InterruptedException {
        boolean locked = false;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            while (timeOut >= 0) {
                long expires = System.currentTimeMillis() + lockTime + 1;
                String expiresStr = String.valueOf(expires); //锁到期时间
                if (jedis.setnx(LOCK, expiresStr) == 1) {
                    locked = true;
                    break;
                }
                String currentValueStr = jedis.get(LOCK); //redis里的时间
                if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                    String oldValueStr = jedis.getSet(LOCK, expiresStr);
                    if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                        locked = true;
                        break;
                    }
                }
                timeOut -= 100;
                Thread.sleep(100);
            }
        }
        if(jedis != null){
            jedis.close();
        }
        return locked;
    }

    @Override
    public boolean lock(long timeOut, long lockTime) throws InterruptedException{
        return acquire(timeOut,lockTime);
    }

    @Override
    public boolean lock(long timeOut) throws InterruptedException{
        return acquire(timeOut,500);
    }

    @Override
    public boolean tryLock() throws InterruptedException{
        return acquire(100,500);
    }

    @Override
    public void unLock() {
        release();
    }

    private void release(){
        Jedis jedis = getJedisClient();
        if(jedis != null){
            jedis.del(LOCK);
        }
    }
}
