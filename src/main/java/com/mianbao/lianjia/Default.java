package com.mianbao.lianjia;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Created by zoujiajian on 2017-4-11.
 */
public class Default {

    private Default(){

    }

    public static HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        return headers;
    }

}
