package com.mianbao.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by zoujiajian on 2017-4-28.
 */
public class UrlParseUtil {

    private UrlParseUtil(){

    }

    /**
     * 去除url 的前半部分 只留下参数部分
     * @param url
     * @return String
     */
    public static String parseArguments(String url){
        String[] mappers;
        String args = null;
        url = url.trim();
        mappers = url.split("[?]");
        if(url.length() > 1){
            if(mappers.length > 1){
                if(mappers[1] != null){
                    args = mappers[1];
                }
            }
        }
        return args;
    }

    /**
     * 解析url中参数部分为为k v
     * @param url
     * @return  Map
     */
    public static Map<String,String> parseArgumentsToMap(String url) {
        if (StringUtils.isEmpty(url)) {
            return Maps.newHashMap();
        }
        Map<String, String> mapper = Maps.newHashMap();
        String[] args = url.split("&");
        for (String arg : args) {
            if (StringUtils.isNotEmpty(arg)) {
                String[] kvMapper = arg.split("=");
                //只有k 没有v 的不存储
                if (kvMapper.length > 1) {
                    mapper.put(kvMapper[0], kvMapper[1]);
                }
            }
        }
        return mapper;
    }

    /**
     * 截取完整url的前缀（去除参数部分）
     * @param url
     * @return
     */
    public static String getURlprefix(String url){
        if(StringUtils.isEmpty(url)){
            throw new IllegalArgumentException("url is empty");
        }
        String[] URL = url.split("[?]");
        return URL[0];
    }
}
