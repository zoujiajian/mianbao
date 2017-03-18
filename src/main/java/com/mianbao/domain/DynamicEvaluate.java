package com.mianbao.domain;

import java.util.Date;

public class DynamicEvaluate {
    private Integer id;

    private Integer dynamicId;

    private String evaluateContent;

    private Integer evaluateUser;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent == null ? null : evaluateContent.trim();
    }

    public Integer getEvaluateUser() {
        return evaluateUser;
    }

    public void setEvaluateUser(Integer evaluateUser) {
        this.evaluateUser = evaluateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}