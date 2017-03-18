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
    public void advice(ProceedingJoinPoint joinPoint){
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();
        Class clazz = target.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            if(method.getName().equals(methodName)){
                //判断拦截方法上是否存在logAround日志注解
                if(method.isAnnotationPresent(LogAround.class)){
                    Object[] arguments = joinPoint.getArgs();
                    try {
                        long startTime = System.currentTimeMillis();
                        Object result = joinPoint.proceed();
                        long endTime = System.currentTimeMillis();
                        String log = getLoggerFormat(method,arguments,result,endTime - startTime);
                        logger.info(log);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        logger.error("logAround is error");
                    }
                }
            }
        }
    }

    /**
     * 组装logger格式
     * @param method
     * @param args
     * @param response
     * @param timeOut
     * @return
     */
    private String getLoggerFormat(Method method, Object[] args, Object response, long timeOut){
        String methodName = method.getName();
        String request = JSON.toJSONString(args);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(methodName).append(" = ").append("request : ").
                     append(request).append(" response : ").
                     append(JSON.toJSONString(response)).append(" timeOut : {").
                     append(timeOut).append("}");

        return stringBuffer.toString();
    }
}
