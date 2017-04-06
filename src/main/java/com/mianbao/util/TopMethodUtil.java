package com.mianbao.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by zoujiajian on 2017-4-7.
 */
public class TopMethodUtil {

    private static final List<String> method = Lists.newArrayList();

    static {
        method.add("");
    }

    public static boolean containsMethodName(String methodName){
        if(StringUtils.isEmpty(methodName)){
            throw new IllegalArgumentException("methodName is empty");
        }
        for(String var : method){
            if(var.equals(methodName)){
                return true;
            }
        }
        return false;
    }
}
