package com.mianbao.aop;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mianbao.annotation.RedisCache;
import com.mianbao.common.Result;
import com.mianbao.pojo.LogAround;
import com.mianbao.service.RedisService;
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

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheParse.class);

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
                    String key = getKey(argument, redisCache);

                    startTime = System.currentTimeMillis();
                    String cacheValue = redisService.getByKey(key);
                    endTime = System.currentTimeMillis();
                    //缓存命中
                    if(cacheValue != null){
                        // TODO 必须有默认的构造函数
                        result = JSON.parseObject(cacheValue,type);
                        if(result != null){
                            logPrint(argument,method.getName(),target.getClass().getName(),
                                    result,startTime,endTime,Boolean.TRUE);
                        }
                        return result;
                    }
                    //缓存未命中但需要缓存
                    try {
                        startTime = System.currentTimeMillis();
                        result = joinPoint.proceed();
                        endTime = System.currentTimeMillis();
                        Result response = (Result) result;
                        //服务正确返回的情况下缓存结果
                        if (response.isSuccess()) {
                            setRedis(key, result);
                        }

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

    private String getKey(Object[] argument, RedisCache cacheAround){
        //加上时间段构成key
        if(cacheAround.time()){
            if(cacheAround.startTime() < 0 || cacheAround.endTime() < 0){
                throw new IllegalArgumentException("time is less zero");
            }
            StringBuffer key = new StringBuffer();
            key.append(cacheAround.startTime()).
                    append(Lists.newArrayList(argument)).
                    append(cacheAround);
            return JSON.toJSONString(key);
        }
        return JSON.toJSONString(argument);
    }

    private void setRedis(String key, Object value){
        redisService.addByKey(key,JSON.toJSONString(value));
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
