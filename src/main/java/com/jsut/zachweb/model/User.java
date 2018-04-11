package com.jsut.zachweb.model;

import java.util.Date;

public class User {
    private Integer userId;

    private String userName;

    private String userPassword;

    private String userType;

    private String userTypeCode;

    private String userVerifyStatus;

    private Date userRegistTime;

    private String userAvatar;

    private String userPhone;

    private String userEmail;

    private String userSex;

    private String userCity;

    private String userSignature;

    private Integer userQq;

    private String userWechat;

    private String userWeibo;

    private Integer userMoney;

    private String userVipGrade;

    private Integer userGrade;

    private String userPreference;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode == null ? null : userTypeCode.trim();
    }

    public String getUserVerifyStatus() {
        return userVerifyStatus;
    }

    public void setUserVerifyStatus(String userVerifyStatus) {
        this.userVerifyStatus = userVerifyStatus == null ? null : userVerifyStatus.trim();
    }

    public Date getUserRegistTime() {
        return userRegistTime;
    }

    public void setUserRegistTime(Date userRegistTime) {
        this.userRegistTime = userRegistTime;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar == null ? null : userAvatar.trim();
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone == null ? null : userPhone.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex == null ? null : userSex.trim();
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity == null ? null : userCity.trim();
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature == null ? null : userSignature.trim();
    }

    public Integer getUserQq() {
        return userQq;
    }

    public void setUserQq(Integer userQq) {
        this.userQq = userQq;
    }

    public String getUserWechat() {
        return userWechat;
    }

    public void setUserWechat(String userWechat) {
        this.userWechat = userWechat == null ? null : userWechat.trim();
    }

    public String getUserWeibo() {
        return userWeibo;
    }

    public void setUserWeibo(String userWeibo) {
        this.userWeibo = userWeibo == null ? null : userWeibo.trim();
    }

    public Integer getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(Integer userMoney) {
        this.userMoney = userMoney;
    }

    public String getUserVipGrade() {
        return userVipGrade;
    }

    public void setUserVipGrade(String userVipGrade) {
        this.userVipGrade = userVipGrade == null ? null : userVipGrade.trim();
    }

    public Integer getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(Integer userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserPreference() {
        return userPreference;
    }

    public void setUserPreference(String userPreference) {
        this.userPreference = userPreference == null ? null : userPreference.trim();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userType='" + userType + '\'' +
                ", userTypeCode='" + userTypeCode + '\'' +
                ", userVerifyStatus='" + userVerifyStatus + '\'' +
                ", userRegistTime=" + userRegistTime +
                ", userAvatar='" + userAvatar + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userCity='" + userCity + '\'' +
                ", userSignature='" + userSignature + '\'' +
                ", userQq=" + userQq +
                ", userWechat='" + userWechat + '\'' +
                ", userWeibo='" + userWeibo + '\'' +
                ", userMoney=" + userMoney +
                ", userVipGrade='" + userVipGrade + '\'' +
                ", userGrade=" + userGrade +
                ", userPreference='" + userPreference + '\'' +
                '}';
    }
}