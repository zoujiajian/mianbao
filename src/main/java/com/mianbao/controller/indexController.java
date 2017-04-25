package com.mianbao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zoujiajian on 2017-3-20.
 */
@RequestMapping(value = "/mianbao")
@Controller
public class IndexController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "sys/index";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register() {
        return "register";
    }


    @RequestMapping(value = "/file",method = RequestMethod.GET)
    public String file() {
        return "file";
    }

}
