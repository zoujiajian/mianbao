package com.mianbao.enums;

import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-21.
 */

@ToString
public enum Response {

    REGION_FAIL(100001,"注册失败");

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
