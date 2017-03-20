package com.mianbao.pojo;


import java.io.Serializable;

/**
 * Created by zoujiajian on 2017-3-20.
 */
public class LogAround implements Serializable{

    private Object[] request;

    private Object response;

    private Long timeOut;

    public LogAround() {

    }

    public Object[] getRequest() {
        return request;
    }

    public void setRequest(Object[] request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "LogAround{" +
                "request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", timeOut=" + timeOut +
                '}';
    }
}
