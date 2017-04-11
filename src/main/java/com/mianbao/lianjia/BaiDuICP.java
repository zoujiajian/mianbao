package com.mianbao.lianjia;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created by zoujiajian on 2017-4-11.
 */
@Service
public class BaiDuICP extends AbstractIcpTemplate{

    @Resource
    private RestTemplate restTemplate;

    private static final String TARGET_URL = "http://www.baidu.com";

    @Override
    protected boolean getEndIndex(char source) {
        if(source == '<'){
            return true;
        }
        return false;
    }

    @Override
    protected String getRemoteBody() {
        HttpEntity<String> entity = new HttpEntity<>(Default.defaultHeaders());
        ResponseEntity<String> jsonResult = restTemplate.exchange(TARGET_URL, HttpMethod.GET,entity,String.class);
        System.out.println("JSON result = " + jsonResult.getBody());
        return jsonResult.getBody();
    }
}
