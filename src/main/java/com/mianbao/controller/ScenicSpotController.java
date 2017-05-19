package com.mianbao.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.mianbao.common.Page;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.ScenicSpotService;
import com.mianbao.service.TokenParseService;
import com.mianbao.vo.ScenicScoreVo;
import com.mianbao.vo.ScenicSpotVo;
import com.mianbao.vo.ScoreListVo;
import com.mianbao.vo.ScoreVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zoujiajian on 2017-4-28.
 */
@Controller
@RequestMapping(value = "/mianbao/travel/scenic")
public class ScenicSpotController {

    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotController.class);

    @Resource
    private ScenicSpotService scenicSpotService;

    @Resource
    private TokenParseService parseService;

    @RequestMapping(value = "/getAllScenicList",method = RequestMethod.GET)
    @ResponseBody
    public Result getAllScenicSpot(){
        return scenicSpotService.getAllScenicSpot();
    }

    @RequestMapping(value = "/addScenicSpot",method = RequestMethod.POST)
    @ResponseBody
    public Result addScenicSpot(@CookieValue(name = "token") String token, HttpServletRequest request){
        return scenicSpotService.addScenicSpot(request,token);
    }

    @RequestMapping(value = "/findScenicByName",method = RequestMethod.GET)
    @ResponseBody
    public Result findScenicByName(@RequestParam(name = "scenicName") String scenicName){
        return scenicSpotService.findByName(scenicName);
    }

    @RequestMapping(value = "/scenicInfo",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String scenicInfo(@RequestParam(name = "id") int id,
                             @RequestParam(name = "pageNo") int pageNo,
                             @RequestParam(name = "pageSize") int pageSize,
                             Model model){

        Result result = scenicSpotService.getScenicSpotInfoWithDynamic(id,pageNo,pageSize);
        if(result.isSuccess()){
            ScenicSpotVo scenicSpotVo = (ScenicSpotVo) result.getData();
            model.addAttribute("data",scenicSpotVo);
        }else{
            Page page = new Page<>();
            page.setRows(Collections.emptyList());
            model.addAttribute("data",page);
        }
        return "scenicInfo";
    }

    @RequestMapping(value = "/collection",method = RequestMethod.GET)
    @ResponseBody
    public Result scenicCollection(@CookieValue(name = "token") String token,
                                   @RequestParam(value = "id") int id){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return Result.getDefaultError(Response.USER_NOT_LOGIN.getMsg());
        }
        return scenicSpotService.collectionScenicSpot(id,userLogin.getId());
    }

    @RequestMapping(value = "/collectionInfo",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String collectionInfo(@CookieValue(name = "token") String token,
                                 @RequestParam(name = "pageNo") int pageNo,
                                 @RequestParam(name = "pageSize") int pageSize,
                                 Model model){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return "sys/index";
        }

        Result result = scenicSpotService.collectionWithLimit(userLogin.getId(),pageNo,pageSize);
        if(result.isSuccess()){
            model.addAttribute("data",result.getData());
        }else{
            Page page = new Page<>();
            page.setRows(Collections.emptyList());
            model.addAttribute("data",page);
        }
        return "collection";
    }

    @RequestMapping(value = "/collectionInfoSearch",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String collectionInfoSearch(@CookieValue(name = "token") String token,
                                       @RequestParam(name = "pageNo") int pageNo,
                                       @RequestParam(name = "pageSize") int pageSize,
                                       @RequestParam(name = "condition") String condition,
                                       Model model){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return "sys/index";
        }

        Result result = scenicSpotService.collectionSearch(condition,userLogin.getId(),pageNo,pageSize);
        if(result.isSuccess()){
            model.addAttribute("data",result.getData());
        }else{
            Page page = new Page<>();
            page.setRows(Collections.emptyList());
            model.addAttribute("data",page);
        }
        return "collection";
    }


    @RequestMapping(value = "center",method = RequestMethod.GET)
    public String center( @RequestParam(name = "pageNo") int pageNo,
                          @RequestParam(name = "pageSize") int pageSize,
                          @RequestParam(name = "condition",required = false) String condition,
                          Model model){
        Map<String,Object> params = Maps.newHashMap();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        if(StringUtils.isNotEmpty(condition)){
            params.put("condition",condition);
        }

        Result result = scenicSpotService.selectAllScenicInfo(params);
        if(result.isSuccess()){
            model.addAttribute("data",result.getData());
        }else{
            model.addAttribute("data",null);
        }
        return "scenicSpotCenter";
    }

    @RequestMapping(value = "revoke",method = RequestMethod.GET)
    @ResponseBody
    public Result revoke(@CookieValue(name = "token") String token,
                         @RequestParam(name = "id") int id){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return Result.getDefaultError(Response.USER_NOT_LOGIN.getMsg());
        }
        return scenicSpotService.revoke(userLogin.getId(),id);
    }

    @RequestMapping(value = "score",method = RequestMethod.POST)
    @ResponseBody
    public Result score(@CookieValue(name = "token") String token,
                        @RequestBody ScoreVo scoreVo){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return Result.getDefaultError(Response.USER_NOT_LOGIN.getMsg());
        }
        return scenicSpotService.scoreScenic(scoreVo,userLogin.getId());
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "scoreList",method = RequestMethod.GET)
    public String releaseScore(@RequestParam(name = "id") int id,
                               @RequestParam(name = "pageNo") int pageNo,
                               @RequestParam(name = "pageSize") int pageSize,
                               Model model){
        Result avgResult = scenicSpotService.avgScore(id);
        Map<String,Object> params = Maps.newHashMap();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        params.put("scenicId",id);
        Result rowsResult = scenicSpotService.selectScoreList(params);

        ScenicScoreVo scenicScoreVo = new ScenicScoreVo();
        scenicScoreVo.setAvgScore(Double.valueOf(avgResult.getData().toString()));
        scenicScoreVo.setPage((Page<ScoreListVo>) rowsResult.getData());
        model.addAttribute("id",id);
        model.addAttribute("data",scenicScoreVo);
        return "score";
    }
}
