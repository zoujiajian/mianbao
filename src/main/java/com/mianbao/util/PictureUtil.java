package com.mianbao.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zoujiajian on 2017-4-29.
 */
public class PictureUtil {

    private PictureUtil(){


    }

    public static String addHttpHost(String picture){
        if(StringUtils.isEmpty(picture)){
            return picture;
        }
        return "http://120.25.234.111:8888/" + picture;
    }
}
