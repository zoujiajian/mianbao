package com.mianbao.aop;

import com.alibaba.fastjson.JSON;
import com.mianbao.common.Result;
import com.mianbao.pojo.LogAroundPojo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by zoujiajian on 2017-4-24.
 */
@Aspect
@Component
public class LogAroundParse {

    private static final Logger logger = LoggerFactory.getLogger(LogAroundParse.class);

    @Pointcut("execution(* com.minabao.controller..*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object advice(ProceedingJoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method currentMethod = methodSignature.getMethod();
        String methodName = currentMethod.getName();
        String className = target.getClass().getSimpleName();

        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            if(currentMethod.isAnnotationPresent(com.mianbao.annotation.LogAround.class)){
                Result methodResult = (Result) result;
                Object[] arguments = joinPoint.getArgs();

                LogAroundPojo logAround = new LogAroundPojo();
                logAround.setClassName(className);
                logAround.setMethodName(methodName);
                logAround.setRequest(arguments);
                logAround.setResponse(JSON.toJSONString(result));
                logAround.setTimeOut(endTime - startTime);
                logAround.setSuccess(methodResult.isSuccess());
                logger.info("monitor log info: " + logAround.toString());
            }
        } catch (Throwable throwable) {
            logger.error("", throwable);
        }finally {
            long elapseTime = System.currentTimeMillis() - startTime;
            String monitorMethodName = className + "." + methodName;
            if(elapseTime > 1000){
                logger.warn(String.format("controller %s elapse %s", monitorMethodName, elapseTime));
            }
        }
        return result;
    }
}
