package com.mianbao.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * Created by zoujiajian on 2017-3-10.
 * 文件上传服务
 */
@Component
public class FileLoadService {

    private static final Logger logger = LoggerFactory.getLogger(FileLoadService.class);

    @Resource
    private PictureService pictureService;

    public String uploadFile(HttpServletRequest request){

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().
                getServletContext());
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxInMemorySize(10960);
        multipartResolver.setMaxUploadSize(10485760000L);

        StringBuilder picture = new StringBuilder();
        //检测是否包含文件
        if(multipartResolver.isMultipart(request)){
            //将request 转换为多个request 保存多个文件
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取文件字段
            Iterator<String> fileNames = multiRequest.getFileNames();
            while(fileNames.hasNext()){
                String fileName = fileNames.next();
                //截取文件后缀名
                String fileExtName = fileName.substring(fileName.lastIndexOf("."));
                MultipartFile file = multiRequest.getFile(fileName);
                try {
                    String pictureAddress = pictureService.uploadPicture(file.getBytes(),fileExtName);
                    picture.append(",").append(pictureAddress);
                } catch (Exception e) {
                    logger.error("upload picture error",e);
                }
            }
        }

        return  picture.toString().replaceFirst(",","");
    }

    /**
     * 是否包含图片
     * @param request
     * @return
     */
    public boolean isContainsFile(HttpServletRequest request){
        if(request != null){
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().
                    getServletContext());
            return multipartResolver.isMultipart(request);
        }
        return false;
    }
}
