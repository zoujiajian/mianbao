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
public class LianJiaICP extends AbstractIcpTemplate{

    @Resource
    private RestTemplate restTemplate;

    private static final String TARGET_URL = "http://www.lianjia.com";


    @Override
    protected boolean getEndIndex(char source) {
        if(source == '|'){
            return true;
        }
        return false;
    }

    @Override
    protected String getRemoteBody() {
        HttpEntity<String> entity = new HttpEntity<>(Default.defaultHeaders());
        ResponseEntity<String> jsonResult = restTemplate.exchange(TARGET_URL, HttpMethod.GET,entity,String.class);
        return jsonResult.getBody();
    }
}
