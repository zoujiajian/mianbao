package com.mianbao.annotation;

import com.alibaba.fastjson.JSONObject;

import java.lang.annotation.*;

/**
 * Created by zoujiajian on 2017-3-20.
 * 缓存命中cache
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisCache {

    /**
     * redis解析之后的类型
     * @return
     */
    Class type() default Object.class;
}
