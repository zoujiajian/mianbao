package com.mianbao.controller;

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
@EnableAutoConfiguration
@RequestMapping("/")
public class indexController {

    @RequestMapping("index")
    public String index(){
        return "hello springBoot";
    }


}
