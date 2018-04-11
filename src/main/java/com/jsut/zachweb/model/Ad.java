package com.jsut.zachweb.model;

import java.math.BigDecimal;
import java.util.Date;

public class Ad {
    private Integer adId;

    private Integer adUserId;

    private String adType;

    private String adTypeCode;

    private String adTitle;

    private String adHtml;

    private String adText;

    private String adPic;

    private Double adPrice;

    private Date adAddTime;

    private Date adLastUpdateTime;

    private Date adStartTime;

    private Date adEndTime;

    private Integer adClickNumber;

    private Integer adCollectNumber;

    private Integer adCommentNumber;

    private Date adLastCommentTime;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(Integer adUserId) {
        this.adUserId = adUserId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType == null ? null : adType.trim();
    }

    public String getAdTypeCode() {
        return adTypeCode;
    }

    public void setAdTypeCode(String adTypeCode) {
        this.adTypeCode = adTypeCode == null ? null : adTypeCode.trim();
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle == null ? null : adTitle.trim();
    }

    public String getAdHtml() {
        return adHtml;
    }

    public void setAdHtml(String adHtml) {
        this.adHtml = adHtml == null ? null : adHtml.trim();
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText == null ? null : adText.trim();
    }

    public String getAdPic() {
        return adPic;
    }

    public void setAdPic(String adPic) {
        this.adPic = adPic == null ? null : adPic.trim();
    }

    public Double getAdPrice() {
        return adPrice;
    }

    public void setAdPrice(Double adPrice) {
        this.adPrice = adPrice;
    }

    public Date getAdAddTime() {
        return adAddTime;
    }

    public void setAdAddTime(Date adAddTime) {
        this.adAddTime = adAddTime;
    }

    public Date getAdLastUpdateTime() {
        return adLastUpdateTime;
    }

    public void setAdLastUpdateTime(Date adLastUpdateTime) {
        this.adLastUpdateTime = adLastUpdateTime;
    }

    public Date getAdStartTime() {
        return adStartTime;
    }

    public void setAdStartTime(Date adStartTime) {
        this.adStartTime = adStartTime;
    }

    public Date getAdEndTime() {
        return adEndTime;
    }

    public void setAdEndTime(Date adEndTime) {
        this.adEndTime = adEndTime;
    }

    public Integer getAdClickNumber() {
        return adClickNumber;
    }

    public void setAdClickNumber(Integer adClickNumber) {
        this.adClickNumber = adClickNumber;
    }

    public Integer getAdCollectNumber() {
        return adCollectNumber;
    }

    public void setAdCollectNumber(Integer adCollectNumber) {
        this.adCollectNumber = adCollectNumber;
    }

    public Integer getAdCommentNumber() {
        return adCommentNumber;
    }

    public void setAdCommentNumber(Integer adCommentNumber) {
        this.adCommentNumber = adCommentNumber;
    }

    public Date getAdLastCommentTime() {
        return adLastCommentTime;
    }

    public void setAdLastCommentTime(Date adLastCommentTime) {
        this.adLastCommentTime = adLastCommentTime;
    }
}