package com.mianbao.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-10.
 * 业务异常类
 */
@Getter
@Setter
@ToString
public class BusinessException extends RuntimeException{

    private String message;

    private int code;

    public BusinessException(){

    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
}
