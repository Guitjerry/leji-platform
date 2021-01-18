package com.macro.mall.dto;

/**
 * @Date 2021/1/17 12:14
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class SessionDto {
    private String openId;
    private String sessionKey;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }
}
