package com.macro.mall.mallwechat.service;
import com.macro.mall.mallwechat.dto.SessionDto;

/**
 * 微信openId相关service
 */
public interface WechatService {
    /**
     * 查询openId
     * @param code
     * @return
     */
    SessionDto getOpenIdByCode(String code);
}
