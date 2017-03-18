package com.mianbao.domain;

import java.util.Date;

public class ScenicSpotEvaluate {
    private Integer id;

    private Integer userId;

    private String evaluateContent;

    private String evaluatePicutre;

    private Date createTime;

    private Byte evaluateStar;

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

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent == null ? null : evaluateContent.trim();
    }

    public String getEvaluatePicutre() {
        return evaluatePicutre;
    }

    public void setEvaluatePicutre(String evaluatePicutre) {
        this.evaluatePicutre = evaluatePicutre == null ? null : evaluatePicutre.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getEvaluateStar() {
        return evaluateStar;
    }

    public void setEvaluateStar(Byte evaluateStar) {
        this.evaluateStar = evaluateStar;
    }
}