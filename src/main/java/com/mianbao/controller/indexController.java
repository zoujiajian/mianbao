package com.mianbao.controller;

import com.mianbao.common.Result;
import com.mianbao.service.DynamicService;
import com.mianbao.service.ScenicSpotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created by zoujiajian on 2017-3-20.
 */
@RequestMapping(value = "/mianbao/travel")
@Controller
public class IndexController {

    @Resource
    private DynamicService dynamicService;

    @Resource
    private ScenicSpotService scenicSpotService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model) {
        Result result = dynamicService.indexSimpleInfo();
        if(result.isSuccess()){
            model.addAttribute("dynamicData",result.getData());
        }
        Result scenicSpot = scenicSpotService.indexScenicSpot();
        if(scenicSpot.isSuccess()){
            model.addAttribute("scenicData",scenicSpot.getData());
        }
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

}
