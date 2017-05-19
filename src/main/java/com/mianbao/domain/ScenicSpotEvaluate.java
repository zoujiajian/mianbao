package com.mianbao.domain;

import java.util.Date;

public class ScenicSpotEvaluate {
    private Integer id;

    private Integer userId;

    private Integer scenicId;

    private String evaluateContent;

    private Date createTime;

    private Double evaluateStar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScenicId() {
        return scenicId;
    }

    public void setScenicId(Integer scenicId) {
        this.scenicId = scenicId;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent == null ? null : evaluateContent.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getEvaluateStar() {
        return evaluateStar;
    }

    public void setEvaluateStar(Double evaluateStar) {
        this.evaluateStar = evaluateStar;
    }
}