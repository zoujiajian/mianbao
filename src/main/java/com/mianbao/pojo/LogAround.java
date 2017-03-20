package com.mianbao.pojo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by zoujiajian on 2017-3-20.
 */
@Getter
@Setter
@ToString
public class LogAround implements Serializable{

    private Object[] request;

    private Object response;

    private Long timeOut;

    private boolean isCache = Boolean.FALSE;

}
