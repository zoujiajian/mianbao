package com.mianbao.controller;

import com.mianbao.annotation.LogAround;
import com.mianbao.annotation.RedisCache;
import com.mianbao.common.Result;
import com.mianbao.dao.DynamicEvaluateMapper;
import com.mianbao.domain.DynamicEvaluate;
import com.mianbao.domain.DynamicEvaluateExample;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zoujiajian on 2017-3-9.
 */
@RestController
@RequestMapping("/test")
public class TestController {


    @RequestMapping("index")
    @RedisCache(type = Result.class)
    public Result<Object> index(String name){
        System.out.println("执行方法");
        return test();
    }

    private Result<Object> test(){
        return new Result<>(Boolean.FALSE,500,"errorMsg",null);
    }

}
