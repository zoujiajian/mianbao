package com.mianbao.controller;

import com.mianbao.common.CacheKey;
import com.mianbao.common.Page;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.pojo.user.UserLogin;
import com.mianbao.service.DynamicService;
import com.mianbao.service.TokenParseService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * Created by zoujiajian on 2017-4-8.
 */
@Controller
@RequestMapping(value = "/mianbao/travel/dynamic")
public class UserDynamicController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Resource
    private DynamicService dynamicService;

    @Resource
    private TokenParseService parseService;

    @RequestMapping(value = "/release",method = RequestMethod.POST)
    @ResponseBody
    public Result releaseDynamic(HttpServletRequest request){
        Result result;
        try{
            result = dynamicService.releaseDynamic(request);
            if(result.isSuccess()){
                return result;
            }else if(result.getErrorMsg().equals(Response.USER_LOGIN_TIMEOUT.getMsg())){
                return Result.getDefaultError("用户尚未登录");
            }
        }catch (Exception e){
            logger.error("releaseDynamic error",e);
        }
        return Result.getDefaultSuccess(null);
    }

    @RequestMapping(value = "/center",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String getAllDynamic(@CookieValue(name = "token") String token,
                                @RequestParam("pageNo") int pageNo,
                                @RequestParam("pageSize") int pageSize, Model model){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return "sys/index";
        }else{
            Result result = dynamicService.getUserAllDynamic(userLogin.getId(),pageNo,pageSize);
            if(result != null){
                model.addAttribute("page", result.getData() );
            }else{
                Page page = new Page<>();
                page.setRows(Collections.emptyList());
                model.addAttribute("page",new Page<>());
            }
        }
        return "center";
    }

    @RequestMapping(value = "/dynamicInfo",method = RequestMethod.GET)
    @SuppressWarnings("unchecked")
    public String getDynamicInfo(@CookieValue(name = "token") String token,
                                 @RequestParam("id") int id,
                                 Model model){
        UserLogin userLogin = parseService.getUserLogin(token);
        if(userLogin == null){
            return "sys/index";
        }else{
            Result result = dynamicService.getDynamicInfo(id);
            if(result.isSuccess()){
                model.addAttribute("data",result.getData());
            }else{
                Page page = new Page<>();
                page.setRows(Collections.emptyList());
                model.addAttribute("data",page);
            }
        }
        return "dynamicInfo";
    }
}
