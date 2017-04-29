package com.mianbao.controller;

import com.mianbao.annotation.LogAround;
import com.mianbao.common.Result;
import com.mianbao.enums.Response;
import com.mianbao.service.FileLoadService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
/**
 * Created by zoujiajian on 2017-4-13.
 */
@RestController
@RequestMapping(value = "/mianbao/travel/picture")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Resource
    private FileLoadService fileLoadService;

    @LogAround
    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public Result uploadPicture(HttpServletRequest request){
        try{
            String address = fileLoadService.uploadFile(request);
            return Result.getDefaultSuccess(address);
        }catch (Exception e){
            logger.error("图片上传失败",e);
        }
        return Result.getDefaultError(Response.UPLOAD_PICTURE_FAIL.getMsg());
    }
}
