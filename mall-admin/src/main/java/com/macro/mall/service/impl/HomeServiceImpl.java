package com.macro.mall.service.impl;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.dto.HomeResultDto;
import com.macro.mall.dto.OmsOrderQueryParam;
import com.macro.mall.enums.OrderStatusTypeEnum;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderExample;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductExample;
import com.macro.mall.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date 2021/2/6 13:59
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private OmsOrderDao omsOrderDao;
    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public HomeResultDto index() {
        HomeResultDto homeResultDto = new HomeResultDto();
        OmsOrderQueryParam omsOrderQueryParam = new OmsOrderQueryParam();
        List<OmsOrder> omsOrders = omsOrderDao.getSimpleOrder(omsOrderQueryParam);

        //总订单与销售总额
        homeResultDto.setAllOrder(omsOrders.size());
        BigDecimal allMoney = new BigDecimal(0L);
        homeResultDto.setAllOrder(omsOrders.size());
        for(OmsOrder omsOrder: omsOrders) {
            allMoney = allMoney.add(omsOrder.getTotalAmount());
        }
        homeResultDto.setAllSale(allMoney.doubleValue());

        //今日订单总数
        BigDecimal todayMoney = new BigDecimal(0L);
        List<OmsOrder> todayOrders = Lists.newArrayList();
        for(OmsOrder omsOrder: omsOrders) {
            Long orderCrtime = DateUtil.parse(DateUtil.format(omsOrder.getCreateTime(),"yyyyMMdd")).getTime();
            Long currTime = DateUtil.parse(DateUtil.format(DateUtil.parseDate(DateUtil.now()),"yyyyMMdd")).getTime();
            if(orderCrtime.equals(currTime)) {
                todayOrders.add(omsOrder);
                todayMoney =  todayMoney.add(omsOrder.getTotalAmount());
            }
        }
        homeResultDto.setTodayOrder(todayOrders.size());
        homeResultDto.setSaleOrder(todayMoney.doubleValue());

        //待付款订单
        Long noPayOrder = omsOrders.stream().filter(omsOrder -> omsOrder.getStatus().equals(OrderStatusTypeEnum.wait.getKey())).count();
        homeResultDto.setNoPayOrder(noPayOrder);
        //待发货订单
        Long noSendOrder = omsOrders.stream().filter(omsOrder -> omsOrder.getStatus().equals(OrderStatusTypeEnum.waitSend.getKey())).count();
        homeResultDto.setNoSendOrder(noSendOrder);
        //已发货订单
        Long hasSendOrder = omsOrders.stream().filter(omsOrder -> omsOrder.getStatus().equals(OrderStatusTypeEnum.hasSend.getKey())).count();
        homeResultDto.setHasSendOrder(hasSendOrder);
        //已完成订单
        Long hasFinshOrder = omsOrders.stream().filter(omsOrder -> omsOrder.getStatus().equals(OrderStatusTypeEnum.hasComplete.getKey())).count();
        homeResultDto.setHasFinshOrder(hasFinshOrder);

        //商品数量
        PmsProductExample pmsProductExample = new PmsProductExample();
        pmsProductExample.or().andDeleteStatusEqualTo(CommonConstant.FLAG_NO);
        Long allGoodCount = pmsProductMapper.countByExample(pmsProductExample);
        homeResultDto.setAllCount(allGoodCount);
        //库存紧张
        PmsProductExample stockExample = new PmsProductExample();
        stockExample.or().andStockGreaterThanOrEqualTo(10).andDeleteStatusEqualTo(CommonConstant.FLAG_NO);
        Long stockLitter = pmsProductMapper.countByExample(stockExample);
        homeResultDto.setStockCount(stockLitter);

        return homeResultDto;
    }
}
