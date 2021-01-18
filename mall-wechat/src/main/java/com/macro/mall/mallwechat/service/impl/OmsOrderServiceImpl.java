package com.macro.mall.mallwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.mallwechat.constant.CommonConstant;
import com.macro.mall.mallwechat.dto.OmsOrderPayParam;
import com.macro.mall.mallwechat.dto.OmsWxAppCart;
import com.macro.mall.mallwechat.enums.OrderStatusTypeEnum;
import com.macro.mall.mallwechat.enums.PayTypeEnum;
import com.macro.mall.mallwechat.service.OmsOrderService;
import com.macro.mall.mapper.OmsCartItemMapper;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.model.OmsOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 订单管理Service实现类
 * Created by macro on 2018/10/11.
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsCartItemMapper omsCartItemMapper;
    @Autowired
    private OmsOrderMapper orderMapper;

    @Override
    public int createOrder(OmsOrderPayParam omsOrderPayParam) {
        OmsOrder omsOrder = new OmsOrder();
        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);
        List<OmsWxAppCart> omsWxAppCarts = omsOrderPayParam.getCarts();
        if (CollectionUtil.isNotEmpty(omsWxAppCarts)) {
            Asserts.fail("请选择商品后下单！");
        }
        omsWxAppCarts.forEach(omsWxAppCart -> {
            //购物车
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setCreateDate(DateUtil.parse(DateUtil.now()));
            omsCartItem.setPrice(BigDecimal.valueOf(omsWxAppCart.getPrice()));
            omsCartItem.setProductBrand(String.valueOf(omsWxAppCart.getBrandId()));
            omsCartItem.setProductName(omsWxAppCart.getProductCategoryName());
            omsCartItem.setProductCategoryId(Long.valueOf(omsWxAppCart.getProductCategoryId()));
            omsCartItemMapper.insert(omsCartItem);
            totalAmount.updateAndGet(v -> v + omsWxAppCart.getPrice());
        });
        omsOrder.setOrderSn(UUID.randomUUID().toString());
        omsOrder.setCreateTime(DateUtil.parse(DateUtil.now()));
        omsOrder.setTotalAmount(BigDecimal.valueOf(totalAmount.get()));
        omsOrder.setPayType(PayTypeEnum.NOPAY.getKey());
        omsOrder.setStatus(OrderStatusTypeEnum.hasComplete.getKey());
        omsOrder.setOrderType(CommonConstant.FLAG_YES);
        return orderMapper.insert(omsOrder);
    }
}
