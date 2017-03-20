package com.mianbao.aop;


import com.alibaba.fastjson.JSON;
import com.mianbao.annotation.LogAround;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by zoujiajian on 2017-3-9.
 * 环绕日志解析
 */
@Aspect
@Component
public class LogAroundParse {

    private static final Logger logger = LoggerFactory.getLogger(LogAroundParse.class);

    @Pointcut("execution(* com.mianbao.controller..*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object advice(ProceedingJoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Object result = null;
        try {
            long startTime = System.currentTimeMillis();
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            for(Method method : methods){
                if(method.getName().equals(methodName)){
                    //判断拦截方法上是否存在logAround日志注解
                    if(method.isAnnotationPresent(LogAround.class)){
                        Object[] arguments = joinPoint.getArgs();

                        com.mianbao.pojo.LogAround logAround = new com.mianbao.pojo.LogAround();
                        logAround.setRequest(arguments);
                        logAround.setResponse(result);
                        logAround.setTimeOut(endTime - startTime);
                        logger.info(JSON.toJSONString(logAround));
                    }
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
