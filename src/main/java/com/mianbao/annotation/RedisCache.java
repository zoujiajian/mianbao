package com.mianbao.annotation;

import com.mianbao.common.Result;

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
    Class type() default Result.class;

    /**
     * 解析结果是否为数组 如果结果为数组 必须传入true
     * @return
     */
    boolean isArray() default false;

    /**
     * 是否带上时间段
     * @return
     */
    boolean time() default false;

    /**
     * 开始时间
     * @return
     */
    long startTime() default 0;

    /**
     * 结束时间
     * @return
     */
    long endTime() default 0;

}
