package com.jsut.zachweb.model;

import java.util.Date;

public class Comment {
    private Integer commentId;

    private Integer commentUserId;

    private Integer commentAdId;

    private String commentContent;

    private Date commentTime;

    private Integer commentLikeNumber;

    private User user;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public Integer getCommentAdId() {
        return commentAdId;
    }

    public void setCommentAdId(Integer commentAdId) {
        this.commentAdId = commentAdId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent == null ? null : commentContent.trim();
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Integer getCommentLikeNumber() {
        return commentLikeNumber;
    }

    public void setCommentLikeNumber(Integer commentLikeNumber) {
        this.commentLikeNumber = commentLikeNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}