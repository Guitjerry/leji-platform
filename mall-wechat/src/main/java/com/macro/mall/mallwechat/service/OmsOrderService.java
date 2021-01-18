package com.macro.mall.mallwechat.service;

import com.macro.mall.mallwechat.dto.OmsOrderPayParam;

/**
 * 订单管理Service
 * Created by macro on 2018/10/11.
 */
public interface OmsOrderService {
    /**
     * 微信下单
     * @param omsOrderPayParam
     * @return
     */
    int createOrder(OmsOrderPayParam omsOrderPayParam);
}
