package com.mianbao.service.Impl;

import com.google.common.collect.Sets;
import com.mianbao.service.RedisService;
import com.mianbao.util.RedisKeyPrefixUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import java.util.Set;

/**
 * Created by zoujiajian on 2017-3-10.
 * redis 客户端
 */
@Service
public class RedisServiceImpl extends RedisConfig implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final String LOCK = "mianbao_lock";

    private String getKeyPrefix(String key){
        return RedisKeyPrefixUtil.getKeyAddPrefix(key);
    }

    @Override
    public String getByKey(String key) {
        checkKey(key);
        key = getKeyPrefix(key);
        String value = null;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                value = jedis.get(key);
            }catch (Exception e){
                logger.error("get key exception : {}",e);
            }finally {
                jedis.close();
            }
        }
        return value;
    }

    @Override
    public boolean delByKey(String key) {
        checkKey(key);
        key = getKeyPrefix(key);
        boolean flag = Boolean.FALSE;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                jedis.del(key);
                flag = Boolean.TRUE;
            }catch (Exception e){
                logger.error("del by key error, key :{},e :{}",key,e);
            }finally {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean addByKey(String key, String value) {
        checkKv(key,value);
        key = getKeyPrefix(key);
        boolean flag = Boolean.FALSE;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                jedis.set(key,value);
                flag = Boolean.TRUE;
            }catch (Exception e){
                logger.error("addByKey error, key :{}, value :{}, e:{}",key,value,e);
            }finally {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean updByKey(String key, String value) {
        checkKv(key,value);
        key = getKeyPrefix(key);
        String val = getByKey(key);
        if(StringUtils.isEmpty(val)){
            return Boolean.FALSE;
        }
        return addByKey(key,value);
    }

    @Override
    public boolean incr(String key) {
        checkKey(key);
        key = getKeyPrefix(key);
        Boolean flag = Boolean.FALSE;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                jedis.incr(key);
                flag = Boolean.TRUE;
            }catch (Exception e){
                logger.error("key incr error",e);
            }finally {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public boolean addToOrderSet(String key, int score, String member) {
        checkKey(key);
        key = getKeyPrefix(key);
        Boolean flag = Boolean.FALSE;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
               jedis.zadd(key,score,member);
                flag = Boolean.TRUE;
            }catch (Exception e){
                logger.error("addToOrderSet error",e);
            }finally {
                jedis.close();
            }
        }
        return flag;
    }

    @Override
    public Set<String> getTopByOrderSet(String key, int top) {
        checkKey(key);
        key = getKeyPrefix(key);
        Set<String> order = Sets.newHashSet();
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                order = jedis.zrevrange(key,0,--top);
            }catch (Exception e){
                logger.error("getTopByOrderSet error",e);
            }finally {
                jedis.close();
            }
        }
        return order;
    }

    @Override
    public boolean addByKeyWithExpire(String key, String val, int expire){
        checkKey(key);
        key = getKeyPrefix(key);
        Boolean flag = addByKey(key,val);
        if(flag){
            Jedis jedis = getJedisClient();
            if(jedis != null){
                try{
                    jedis.set(key,val);
                    jedis.expire(key,expire);
                    return Boolean.TRUE;
                }catch (Exception e){
                    logger.error("addByKeyWithExpire error",e);
                }finally {
                    jedis.close();
                }
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public long delOrderSetBetweenXAndY(String key , long x ,long y){
        checkKey(key);
        key = getKeyPrefix(key);
        Long number = null;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                number = jedis.zremrangeByRank (key,x,y);
            }catch (Exception e){
                logger.error("从有序集合删除指定区间元素失败,key : {}, x:{},y:{}",key,x,y);
            }finally {
                jedis.close();
            }
        }
        return number == null ? 0 : number;
    }

    @Override
    public double orderSetScoreIncrByMember(String key,String member){
        checkKey(key);
        key = getKeyPrefix(key);
        Double score = null;
        Jedis jedis = getJedisClient();
        if(jedis != null){
            try{
                score = jedis.zincrby (key,1,member);
            }catch (Exception e){
                logger.error("指定成员的分数加1失败,key : {},member : {}",key,member);
            }finally {
                jedis.close();
            }
        }
        return score == null? 0 : score;
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

    @Override
    public boolean exists(String key) {
        checkKey(key);
        key = getKeyPrefix(key);
        Jedis jedis = getJedisClient();
        boolean exists = false;
        if(jedis != null){
           try {
               exists = jedis.exists(key);
           }catch (Exception e){
               logger.error("exists exception",e);
           }finally {
               jedis.close();
           }
        }
        return exists;
    }

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

    private void release(){
        Jedis jedis = getJedisClient();
        if(jedis != null){
            jedis.del(LOCK);
        }
    }
}
