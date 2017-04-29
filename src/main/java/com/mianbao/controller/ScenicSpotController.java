package com.mianbao.controller;

import com.mianbao.common.Result;
import com.mianbao.service.ScenicSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zoujiajian on 2017-4-28.
 */
@RestController
@RequestMapping(value = "/mianbao/travel/scenic")
public class ScenicSpotController {

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotController.class);

    @Resource
    private ScenicSpotService scenicSpotService;

    @RequestMapping(value = "/getAllScenicList",method = RequestMethod.GET)
    public Result getAllScenicSpot(){
        return scenicSpotService.getAllScenicSpot();
    }

    @RequestMapping(value = "/addScenicSpot",method = RequestMethod.POST)
    public Result addScenicSpot(@CookieValue(name = "token") String token, HttpServletRequest request){
        return scenicSpotService.addScenicSpot(request,token);
    }

    @RequestMapping(value = "/findScenicByName",method = RequestMethod.GET)
    public Result findScenicByName(@RequestParam(name = "scenicName") String scenicName){
        return scenicSpotService.findByName(scenicName);
    }
}
