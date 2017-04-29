package com.mianbao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zoujiajian on 2017-3-20.
 */
@RequestMapping(value = "/mianbao/travel")
@Controller
public class IndexController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index() {
        return "sys/index";
    }

    @RequestMapping(value = "/user/register",method = RequestMethod.GET)
    public String register() {
        return "register";
    }


    @RequestMapping(value = "/user/release",method = RequestMethod.GET)
    public String file() {
        return "file";
    }

    @RequestMapping(value = "/user/login",method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/scenic/addScenicSpot",method = RequestMethod.GET)
    public String addScenicSpot(){return "addScenicSpot";}

    @RequestMapping(value = "/user/center",method = RequestMethod.GET)
    public String center(){return "center";}
}
