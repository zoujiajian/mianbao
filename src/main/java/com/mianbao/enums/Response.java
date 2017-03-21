package com.mianbao.enums;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-21.
 */
@Getter
@Setter
@ToString
public enum Response {

    REGION_FAIL(100001,"注册失败");

    private int code;

    private String msg;

    Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
