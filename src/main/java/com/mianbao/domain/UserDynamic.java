package com.mianbao.domain;

import java.util.Date;

public class UserDynamic {
    private Integer id;

    private Integer userId;

    private String dynamicTitle;

    private String dynamicContent;

    private String dynamicPicture;

    private String scenicSpotIds;

    private Date createtime;

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

    public String getDynamicPicture() {
        return dynamicPicture;
    }

    public void setDynamicPicture(String dynamicPicture) {
        this.dynamicPicture = dynamicPicture == null ? null : dynamicPicture.trim();
    }

    public String getScenicSpotIds() {
        return scenicSpotIds;
    }

    public void setScenicSpotIds(String scenicSpotIds) {
        this.scenicSpotIds = scenicSpotIds == null ? null : scenicSpotIds.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}