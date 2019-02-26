package com.appkwan.exertion.feature.comment;

public class Comment {
    private String comment;
    private String userId;

    public Comment() {
    }

    public Comment(String comment, String userId) {
        this.comment = comment;
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
