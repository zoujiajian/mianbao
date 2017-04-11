package com.mianbao.lianjia;

import com.google.common.collect.Maps;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * Created by zoujiajian on 2017-4-11.
 */
public class ICPFactory {

    private static Map<String,RemoteGetICP> remoteGetICPMap = Maps.newHashMap();

    static {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext.xml");
        remoteGetICPMap.put("baidu",applicationContext.getBean(BaiDuICP.class));
        remoteGetICPMap.put("lianjia",applicationContext.getBean(LianJiaICP.class));
    }

    private ICPFactory(){

    }

    public static BaiDuICP getBaiduIcp(){
        return (BaiDuICP)remoteGetICPMap.get("baidu");
    }


    public static LianJiaICP getLianjiaIcp(){
        return (LianJiaICP)remoteGetICPMap.get("lianjia");
    }
}
