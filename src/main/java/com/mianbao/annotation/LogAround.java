package com.mianbao.annotation;

import java.lang.annotation.*;

/**
 * Created by zoujiajian on 2017-3-9.
 * 自定义环绕日志
 * 支持方法注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LogAround {
}
