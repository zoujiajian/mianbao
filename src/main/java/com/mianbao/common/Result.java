package com.mianbao.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by zoujiajian on 2017-3-10.
 */
@Getter
@Setter
@ToString
public class Result<T> {

    private boolean isSuccess;

    private int code;

    private String errorMsg;

    private T data;

    public Result() {
    }

    public Result(boolean isSuccess, int code, String errorMsg, T data) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static Result getDefaultSuccess(Object data){
        return new Result<>(Boolean.TRUE,200,null,data);
    }

    public static Result getDefaultError(String errorMsg){
        return new Result<>(Boolean.FALSE,500,errorMsg,null);
    }
}
