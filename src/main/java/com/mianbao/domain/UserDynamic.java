package com.mianbao.domain;

import java.util.Date;

public class UserDynamic {
    private Integer dynamicId;

    private Integer userId;

    private String dynamicTitle;

    private String dynamicContent;

    private String dynamicPicutre;

    private Integer scenicSpot;

    private Date createtime;

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDynamicTitle() {
        return dynamicTitle;
    }

    public void setDynamicTitle(String dynamicTitle) {
        this.dynamicTitle = dynamicTitle == null ? null : dynamicTitle.trim();
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent == null ? null : dynamicContent.trim();
    }

    public String getDynamicPicutre() {
        return dynamicPicutre;
    }

    public void setDynamicPicutre(String dynamicPicutre) {
        this.dynamicPicutre = dynamicPicutre == null ? null : dynamicPicutre.trim();
    }

    public Integer getScenicSpot() {
        return scenicSpot;
    }

    public void setScenicSpot(Integer scenicSpot) {
        this.scenicSpot = scenicSpot;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}