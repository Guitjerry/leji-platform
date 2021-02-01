package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.common.util.CustomUUID;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.dao.OmsOrderOperateHistoryDao;
import com.macro.mall.dto.*;
import com.macro.mall.enums.OrderPaySourceEnum;
import com.macro.mall.enums.OrderStatusTypeEnum;
import com.macro.mall.enums.PayTypeEnum;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.query.MarkOrderPayInfoQuery;
import com.macro.mall.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 订单管理Service实现类
 * Created by macro on 2018/10/11.
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderDao orderDao;
    @Autowired
    private OmsOrderOperateHistoryDao orderOperateHistoryDao;
    @Autowired
    private OmsOrderOperateHistoryMapper orderOperateHistoryMapper;
    @Autowired
    private OmsCartItemMapper omsCartItemMapper;
    @Autowired
    private OrderCombineManager orderCombineManager;
    @Autowired
    private UmsMemberMapper umsMemberMapper;
    @Autowired
    private OmsOrderItemMapper omsOrderItemMapper;
    @Autowired
    private OmsOrderMapper omsOrderMapper;
    @Override
    public List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return orderDao.getList(queryParam);
    }

    @Override
    public int delivery(List<OmsOrderDeliveryParam> deliveryParamList) {
        //批量发货
        int count = orderDao.delivery(deliveryParamList);
        //添加操作记录
        List<OmsOrderOperateHistory> operateHistoryList = deliveryParamList.stream()
                .map(omsOrderDeliveryParam -> {
                    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
                    history.setOrderId(omsOrderDeliveryParam.getOrderId());
                    history.setCreateTime(new Date());
                    history.setOperateMan("后台管理员");
                    history.setOrderStatus(2);
                    history.setNote("完成发货");
                    return history;
                }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(operateHistoryList);
        return count;
    }

    @Override
    public int close(List<Long> ids, String note) {
        OmsOrder record = new OmsOrder();
        record.setStatus(4);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        int count = orderMapper.updateByExampleSelective(record, example);
        List<OmsOrderOperateHistory> historyList = ids.stream().map(orderId -> {
            OmsOrderOperateHistory history = new OmsOrderOperateHistory();
            history.setOrderId(orderId);
            history.setCreateTime(new Date());
            history.setOperateMan("后台管理员");
            history.setOrderStatus(4);
            history.setNote("订单关闭:"+note);
            return history;
        }).collect(Collectors.toList());
        orderOperateHistoryDao.insertList(historyList);
        return count;
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrder record = new OmsOrder();
        record.setDeleteStatus(1);
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andIdIn(ids);
        return orderMapper.updateByExampleSelective(record, example);
    }

    @Override
    public OmsOrderDetail detail(Long id) {
        return orderDao.getDetail(id);
    }

    @Override
    public int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam) {
        OmsOrder order = new OmsOrder();
        order.setId(receiverInfoParam.getOrderId());
        order.setReceiverName(receiverInfoParam.getReceiverName());
        order.setReceiverPhone(receiverInfoParam.getReceiverPhone());
        order.setReceiverPostCode(receiverInfoParam.getReceiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.getReceiverDetailAddress());
        order.setReceiverProvince(receiverInfoParam.getReceiverProvince());
        order.setReceiverCity(receiverInfoParam.getReceiverCity());
        order.setReceiverRegion(receiverInfoParam.getReceiverRegion());
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        //插入操作记录
        saveOrderHistory(receiverInfoParam.getOrderId(), receiverInfoParam.getStatus(), "修改收货人信息：");
        return count;
    }

    @Override
    public int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam) {
      OmsOrder order = new OmsOrder();
      order.setId(moneyInfoParam.getOrderId());
      order.setFreightAmount(moneyInfoParam.getFreightAmount());
      order.setDiscountAmount(moneyInfoParam.getDiscountAmount());
      order.setModifyTime(new Date());
      int count = orderMapper.updateByPrimaryKeySelective(order);
      //插入操作记录
      saveOrderHistory(moneyInfoParam.getOrderId(), moneyInfoParam.getStatus(), "修改费用信息：");
      return count;
    }

    @Override
    public int updateNote(Long id, String note, Integer status) {
        OmsOrder order = new OmsOrder();
        order.setId(id);
        order.setNote(note);
        order.setModifyTime(new Date());
        int count = orderMapper.updateByPrimaryKeySelective(order);
        OmsOrderOperateHistory history = new OmsOrderOperateHistory();
        saveOrderHistory(id, status, "修改备注信息："+note);
        return count;
    }

    private void createAppCartByOrder(List<OmsWxAppCart> omsWxAppCarts) {
        //记录购物车
        omsWxAppCarts.forEach(omsWxAppCart -> {
            //购物车
            OmsCartItem omsCartItem = new OmsCartItem();
            omsCartItem.setCreateDate(DateUtil.parse(DateUtil.now()));
            omsCartItem.setPrice(BigDecimal.valueOf(omsWxAppCart.getPrice()));
            omsCartItem.setProductBrand(String.valueOf(omsWxAppCart.getBrandId()));
            omsCartItem.setProductName(omsWxAppCart.getProductCategoryName());
            omsCartItem.setProductCategoryId(omsWxAppCart.getProductCategoryId());
            omsCartItemMapper.insert(omsCartItem);
        });
    }

    private void createOrderItemByOrder(List<OmsWxAppCart> omsWxAppCarts, OmsOrder omsOrder) {
        omsWxAppCarts.forEach(omsWxAppCart -> {
            OmsOrderItem omsOrderItem = new OmsOrderItem();
            omsOrderItem.setProductSn(omsOrder.getOrderSn());
            omsOrderItem.setOrderId(omsOrder.getId());
            omsOrderItem.setProductId(omsWxAppCart.getId());
            omsOrderItem.setProductPic(omsWxAppCart.getPic());
            omsOrderItem.setProductBrand(omsWxAppCart.getBrandName());
            omsOrderItem.setProductName(omsWxAppCart.getName());
            omsOrderItem.setProductPrice(BigDecimal.valueOf(omsWxAppCart.getPrice()));
            omsOrderItem.setProductQuantity(omsWxAppCart.getCount());
            omsOrderItem.setProductCategoryId(omsWxAppCart.getProductCategoryId());
            omsOrderItemMapper.insertSelective(omsOrderItem);
        });
    }


    @Override
    @Transactional
    public int createOrder(OmsOrderPayParam omsOrderPayParam) {
        //
        OmsCompanyAddress omsCompanyAddress =  omsOrderPayParam.getAddresses();
        if(ObjectUtil.isNull(omsCompanyAddress)) {
            Asserts.fail("请选择地址！");
        }
        OmsOrder omsOrder = new OmsOrder();
        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);
        List<OmsWxAppCart> omsWxAppCarts = omsOrderPayParam.getCarts();
        if (CollectionUtil.isEmpty(omsWxAppCarts)) {
            Asserts.fail("请选择商品后下单！");
        }
        //优惠信息
        AllCartDiscountDto allCartDiscountDto = orderCombineManager.queryDiscount(omsWxAppCarts, omsOrderPayParam.getMemberId());
        //计算可获得积分

        UmsMember member = umsMemberMapper.selectByPrimaryKey(omsOrderPayParam.getMemberId());
        omsOrder.setMemberId(omsOrderPayParam.getMemberId());
        omsOrder.setCouponId(allCartDiscountDto.getCouponId());
        omsOrder.setOrderSn(String.valueOf(CustomUUID.getInstance(0, 0).generate()));
        omsOrder.setCreateTime(DateUtil.parse(DateUtil.now()));
        omsOrder.setMemberUsername(member.getUsername());
        omsOrder.setTotalAmount(BigDecimal.valueOf(allCartDiscountDto.getAllMoney()));
        omsOrder.setPayAmount(BigDecimal.valueOf(allCartDiscountDto.getLastDiscountMoney()));
        omsOrder.setPromotionAmount(BigDecimal.valueOf(allCartDiscountDto.getPromotionAmount()));
        omsOrder.setCouponAmount(BigDecimal.valueOf(allCartDiscountDto.getCouponMoney()));
        omsOrder.setPayType(PayTypeEnum.NOPAY.getKey());
        omsOrder.setSourceType(OrderPaySourceEnum.WECHAT.getKey());
        omsOrder.setStatus(OrderStatusTypeEnum.wait.getKey());
        omsOrder.setReceiverPhone(omsCompanyAddress.getPhone());
        omsOrder.setReceiverName(omsCompanyAddress.getName());
        omsOrder.setDeleteStatus(CommonConstant.FLAG_NO);
        omsOrder.setReceiverProvince(omsCompanyAddress.getProvince());
        omsOrder.setReceiverCity(omsCompanyAddress.getCity());
        omsOrder.setReceiverRegion(omsCompanyAddress.getRegion());
        omsOrder.setReceiverDetailAddress(omsCompanyAddress.getDetailAddress());
        int count = orderMapper.insert(omsOrder);
        //记录订单明细项
        createOrderItemByOrder(omsWxAppCarts, omsOrder);
        return  count;
    }

    @Override
    public int remarkOrder(MarkOrderPayInfoQuery markOrderPayInfoQuery) {
        //无效订单记录订单状态
        OmsOrder omsOrder = omsOrderMapper.selectByPrimaryKey(markOrderPayInfoQuery.getOrderId());
        omsOrder.setPayAmount(BigDecimal.valueOf(markOrderPayInfoQuery.getPayFee()));
        omsOrder.setStatus(OrderStatusTypeEnum.waitSend.getKey());
        int count = omsOrderMapper.updateByPrimaryKeySelective(omsOrder);

        saveOrderHistory(markOrderPayInfoQuery.getOrderId(), omsOrder.getStatus(), "标记为正式订单");
        //开始打印订单
        return count;
    }

  @Override
  public int sendOrder(OmsOrder omsOrder) {
    omsOrder.setStatus(OrderStatusTypeEnum.hasSend.getKey());
    int count = omsOrderMapper.updateByPrimaryKeySelective(omsOrder);
    saveOrderHistory(omsOrder.getId(), omsOrder.getStatus(), "填写发货物流信息");
    return count;
  }

  private void saveOrderHistory(Long orderId, Integer status, String note) {
    OmsOrderOperateHistory history = new OmsOrderOperateHistory();
    history.setOrderId(orderId);
    history.setCreateTime(new Date());
    history.setOperateMan("后台管理员");
    history.setOrderStatus(status);
    history.setNote(note);
    orderOperateHistoryMapper.insert(history);
  }
}
