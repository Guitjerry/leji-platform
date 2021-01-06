package com.macro.mall.service.impl;

import cn.hutool.core.codec.Base32;
import com.alibaba.fastjson.JSONObject;
import com.macro.mall.common.constant.AuthConstant;
import com.macro.mall.service.WechatService;
import com.macro.mall.util.ApacheHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @Date 2020/12/6 14:03
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {
    @Override
    public String getOpenIdByCode(String code) {
        try {
            String path = "?appid=" + new String(Base32.decode(AuthConstant.appId)) + "&secret=" + new String(Base32.decode(AuthConstant.secret)) + "&js_code=" + code + "&grant_type=authorization_code";
            HttpResponse response = ApacheHttpUtil.doGet("https://api.weixin.qq.com", "/sns/jscode2session" + path, "GET", new HashMap<>(), null);
            String jsonString = EntityUtils.toString(response.getEntity());
            Assert.notNull(jsonString, "OpenId查询异常");
            JSONObject responseJsonObj = JSONObject.parseObject(jsonString);
            log.info("responseJsonObj {}", responseJsonObj.toJSONString());
            String openId = responseJsonObj.getString("openid");
            String sessionKey = responseJsonObj.getString("session_key");
            if (StringUtils.isEmpty(openId)) {
                Assert.notNull(openId, "OpenId查询异常");
            }
            return openId;
        } catch (Exception e) {
            log.error("getOpenIdByCode", e);
            Assert.notNull(null, "OpenId查询异常");

        }
        return null;
    }
}
