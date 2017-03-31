package com.mianbao.service.Impl;

import com.google.common.collect.Sets;
import com.mianbao.service.RedisService;
import com.mianbao.util.RedisKeyPrefix;
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

    private String getKeyPrefix(String key){
        return RedisKeyPrefix.getKeyAddPrefix(key);
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
        key = getKeyPrefix(key);
        Boolean flag = addByKey(key,val);
        if(flag){
            Jedis jedis = getJedisClient();
            if(jedis != null){
                try{
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
}
