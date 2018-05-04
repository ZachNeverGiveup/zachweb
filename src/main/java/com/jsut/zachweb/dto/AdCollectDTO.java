package com.jsut.zachweb.dto;

import com.jsut.zachweb.model.Ad;

import java.util.Date;

public class AdCollectDTO {
    private Ad ad;
    private Date collectTime;

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
