package com.mianbao.domain;

import java.util.Date;

public class ScenicSpot {
    private Integer scenicSpotId;

    private String scenicSpotName;

    private String scenicSpotInfo;

    private String scenicSpotPicutre;

    private Date scenicCreatetime;

    private Integer createUser;

    public Integer getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(Integer scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public String getScenicSpotName() {
        return scenicSpotName;
    }

    public void setScenicSpotName(String scenicSpotName) {
        this.scenicSpotName = scenicSpotName == null ? null : scenicSpotName.trim();
    }

    public String getScenicSpotInfo() {
        return scenicSpotInfo;
    }

    public void setScenicSpotInfo(String scenicSpotInfo) {
        this.scenicSpotInfo = scenicSpotInfo == null ? null : scenicSpotInfo.trim();
    }

    public String getScenicSpotPicutre() {
        return scenicSpotPicutre;
    }

    public void setScenicSpotPicutre(String scenicSpotPicutre) {
        this.scenicSpotPicutre = scenicSpotPicutre == null ? null : scenicSpotPicutre.trim();
    }

    public Date getScenicCreatetime() {
        return scenicCreatetime;
    }

    public void setScenicCreatetime(Date scenicCreatetime) {
        this.scenicCreatetime = scenicCreatetime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }
}