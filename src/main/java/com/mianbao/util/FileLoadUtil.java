package com.mianbao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zoujiajian on 2017-3-10.
 * 文件上传辅助类
 */
public class FileLoadUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileLoadUtil.class);

    private static final String PATH_PREFIX = "E:/scenicSpotPicutre";

    private FileLoadUtil(){

    }

    public static String load(HttpServletRequest request){
        long currentTime = System.currentTimeMillis();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext()
        );
        StringBuilder scenicSpot = new StringBuilder();
        //检测是否包含文件
        if(multipartResolver.isMultipart(request)){
            //将request 转换为多个request 保存多个文件
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileName = multiRequest.getFileNames();
            while(fileName.hasNext()){
                MultipartFile file = multiRequest.getFile(fileName.next());
                if(file != null){
                   StringBuilder picutre = new StringBuilder(PATH_PREFIX);
                   //添加当前时间 防止文件重名
                   picutre.append(file.getOriginalFilename()).
                           append(currentTime);
                   scenicSpot.append(picutre).append(",");
                    try {
                        file.transferTo(new File(picutre.toString()));
                    } catch (IOException e) {
                        logger.error("file transferTo error");
                    }
                }
            }
        }
        return scenicSpot.toString();
    }
}
