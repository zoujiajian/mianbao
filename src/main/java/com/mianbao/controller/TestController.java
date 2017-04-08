package com.mianbao.controller;

import com.mianbao.annotation.RedisCache;
import com.mianbao.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zoujiajian on 2017-3-9.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RedisCache()
    @RequestMapping("index")
    public Result<Object> index(String name){
        System.out.println("执行方法");
        return test();
    }

    private Result<Object> test(){
        return new Result<>(Boolean.TRUE,500,"errorMsg",null);
    }

}
