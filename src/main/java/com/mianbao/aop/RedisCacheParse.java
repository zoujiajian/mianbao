package com.mianbao.aop;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mianbao.annotation.RedisCache;
import com.mianbao.common.Result;
import com.mianbao.pojo.LogAround;
import com.mianbao.service.RedisService;
import com.mianbao.util.Md5Util;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Created by zoujiajian on 2017-3-20.
 * 缓存解析
 */
@Aspect
@Component
public class RedisCacheParse {

    private static final Logger logger = LoggerFactory.getLogger("cache");


    private static final int EXPIRE =  60 * 60;

    private static final String CACHE_PREFIX = "mianbao";

    @Resource
    private RedisService redisService;

    @Pointcut("execution(* com.mianbao.service..*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    @SuppressWarnings("unchecked")
    public Object around(ProceedingJoinPoint joinPoint){
        Object result = null;
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Object[] argument = joinPoint.getArgs();
        Long startTime,endTime;

        for(Method method : methods){
            if(method.getName().equals(methodName)){
                if(method.isAnnotationPresent(RedisCache.class)){
                    RedisCache redisCache = method.getAnnotation(RedisCache.class);
                    Class type = redisCache.type();
                    String key = getKey(argument, redisCache, joinPoint);

                    startTime = System.currentTimeMillis();
                    String cacheValue = redisService.getByKey(key);
                    endTime = System.currentTimeMillis();
                    //缓存命中
                    if(cacheValue != null){
                        // TODO 必须有默认的构造函数
                        if(redisCache.isArray()){
                            result = JSON.parseArray(cacheValue,type);
                        }else{
                            result = JSON.parseObject(cacheValue,type);
                        }
                        if(result != null){
                            logPrint(argument,method.getName(),target.getClass().getName(),
                                    result,startTime,endTime,Boolean.TRUE);
                        }
                        return result;
                    }
                    //TODO 这里有个问题 现在判断的标准是方法没有抛出异常则表明成功
                    try {
                        startTime = System.currentTimeMillis();
                        result = joinPoint.proceed();
                        endTime = System.currentTimeMillis();
                        setRedis(key, result);

                        logPrint(argument,methodName,target.getClass().getName(),
                                result,startTime,endTime,Boolean.FALSE);
                        return result;
                    }catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
        try{
            result = joinPoint.proceed();
            return result;

        }catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return  result;
    }

    private String getKey(Object[] argument, RedisCache cacheAround, ProceedingJoinPoint joinPoint){

        StringBuilder key = new StringBuilder(CACHE_PREFIX).append("_").
                append(joinPoint.getSignature().getName()).append("_");
        //加上时间段构成key
        if(cacheAround.time()){
            if(cacheAround.startTime() < 0 || cacheAround.endTime() < 0){
                throw new IllegalArgumentException("time is less zero");
            }
            key.append(cacheAround.startTime()).
                    append("_").
                    append(cacheAround.endTime()).
                    append("_");
        }

        if(argument.length > 0){
            int count = 0;
            for(Object obj : argument){
                key.append(JSON.toJSONString(obj));
                count ++ ;
                if(count < argument.length){
                    key.append("_");
                }
            }
        }else{
            //参数不存在 在加上类的权限定名 保证key唯一
            key.append(joinPoint.getClass().getName());
        }

        logger.info("方法 ：{} ,redis key :{}" , joinPoint.getTarget().getClass().getSimpleName()
                + "_"+ joinPoint.getSignature().getName() , key);
        return JSON.toJSONString(Md5Util.getMD5Key(key.toString()));
    }

    private void setRedis(String key, Object value){
        redisService.addByKeyWithExpire(key,JSON.toJSONString(value),EXPIRE);
    }

    private void logPrint(Object[] arguments ,String methodName, String className , Object result,
                          Long startTime, Long endTime, boolean cache){

        LogAround logAround = new LogAround();
        logAround.setRequest(arguments);
        logAround.setResponse(result);
        logAround.setTimeOut(endTime - startTime);
        logAround.setMethodName(methodName);
        logAround.setClassName(className);
        logAround.setCache(cache);

        logger.info(JSON.toJSONString(logAround));
    }
}
