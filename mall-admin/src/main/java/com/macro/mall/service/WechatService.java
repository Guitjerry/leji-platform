package com.macro.mall.service;

/**
 * 微信openId相关service
 */
public interface WechatService {
    /**
     * 查询openId
     * @param code
     * @return
     */
    String getOpenIdByCode(String code);
}
