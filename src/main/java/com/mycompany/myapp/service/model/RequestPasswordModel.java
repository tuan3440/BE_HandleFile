package com.mycompany.myapp.service.model;

public class RequestPasswordModel {

    private long userId;
    private String resetKey;
    private long expiredTime;

    public RequestPasswordModel(long userId, String resetKey, long expiredTime) {
        this.userId = userId;
        this.resetKey = resetKey;
        this.expiredTime = expiredTime;
    }

    public RequestPasswordModel() {}

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
