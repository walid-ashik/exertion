package com.appkwan.exertion.feature.ratinghistory;

public class Rating {
    private int rate;
    private String userId;

    public Rating() {
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
