package com.jsut.zachweb.model;

import java.util.Date;

public class UserRelation {
    private String sysId;

    private Integer userId1;

    private Integer userId2;

    private Date userAddTime;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId == null ? null : sysId.trim();
    }

    public Integer getUserId1() {
        return userId1;
    }

    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }

    public Date getUserAddTime() {
        return userAddTime;
    }

    public void setUserAddTime(Date userAddTime) {
        this.userAddTime = userAddTime;
    }
}