package com.macro.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.common.util.BeanCopyUtil;
import com.macro.mall.common.util.CustomUUID;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.dao.OmsOrderOperateHistoryDao;
import com.macro.mall.dto.*;
import com.macro.mall.enums.CouponStatusEnum;
import com.macro.mall.enums.OrderPaySourceEnum;
import com.macro.mall.enums.OrderStatusTypeEnum;
import com.macro.mall.enums.PayTypeEnum;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.macro.mall.print.CustomerTicketPrint;
import com.macro.mall.print.PrintCustomer;
import com.macro.mall.query.MarkOrderPayInfoQuery;
import com.macro.mall.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private OmsOrderDao omsOrderDao;
    @Autowired
    private PrintCustomer printCustomer;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;
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
            history.setNote("订单关闭:" + note);
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
        OmsOrderDetail omsOrderDetail = orderDao.getDetail(id);
        List<OmsOrderItem>  omsOrderItems = omsOrderDetail.getOrderItemList();
        final Integer[] allCount = {0};
        if(CollectionUtil.isNotEmpty(omsOrderItems)) {
            omsOrderItems.forEach(omsOrderItem -> {
                allCount[0] = allCount[0] + omsOrderItem.getProductQuantity();
            });
        }
        omsOrderDetail.setAllCount(allCount[0]);
        return omsOrderDetail;
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
        saveOrderHistory(id, status, "修改备注信息：" + note);
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

    private void updateGoodByOrder(List<OmsWxAppCart> omsWxAppCarts, UmsMember umsMember) {
        Integer integration = 0;
        for (OmsWxAppCart omsWxAppCart : omsWxAppCarts) {
            PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(omsWxAppCart.getId());
            Integer payCount = omsWxAppCart.getCount();//买的商品数量
            Integer allCount = pmsProduct.getStock();//商品总库存
            Integer saleCount = pmsProduct.getSale();//销量
            pmsProduct.setSale(payCount + saleCount);
            pmsProduct.setStock(allCount - payCount);
            pmsProductMapper.updateByPrimaryKeySelective(pmsProduct);
            Integer giftPoint = pmsProduct.getGiftPoint();
            if (ObjectUtil.isNotNull(giftPoint)) {
                integration = integration + payCount * giftPoint;
            }
        }
        if(integration>0) {
            umsMember.setIntegration(integration);
            umsMemberMapper.updateByPrimaryKeySelective(umsMember);
        }

    }

    /**
     * 1.验证地址
     * 2.验证商品
     * 3.验证商品是否上线
     * 4.验证商品库存
     * @param omsOrderPayParam
     */
    private void vaildOrderParam(OmsOrderPayParam omsOrderPayParam) {
        OmsCompanyAddress omsCompanyAddress = omsOrderPayParam.getAddresses();
        if (ObjectUtil.isNull(omsCompanyAddress)) {
            Asserts.fail("请选择地址！");
        }
        if (CollectionUtil.isEmpty(omsOrderPayParam.getCarts())) {
            Asserts.fail("请选择商品后下单！");
        }
        List<OmsWxAppCart> carts = omsOrderPayParam.getCarts();
        if(CollectionUtil.isNotEmpty(carts)) {
            carts.forEach(omsWxAppCart -> {
                Long productId = omsWxAppCart.getId();
                PmsProduct pmsProduct = pmsProductMapper.selectByPrimaryKey(productId);
                if(pmsProduct.getStock()- omsWxAppCart.getCount()<0) {
                    Asserts.fail("商品【" + pmsProduct.getName() + "】库存不足");
                }
                if(pmsProduct.getDeleteStatus().equals(CommonConstant.FLAG_YES)) {
                    Asserts.fail("商品【" + pmsProduct.getName() + "】已删除");
                }
                if(pmsProduct.getPublishStatus().equals(CommonConstant.FLAG_NO)) {
                    Asserts.fail("商品【" + pmsProduct.getName() + "】已下架");
                }
            });
        }
    }

    @Override
    @Transactional
    public int createOrder(OmsOrderPayParam omsOrderPayParam) {
        OmsCompanyAddress omsCompanyAddress = omsOrderPayParam.getAddresses();
        //下单验证
        vaildOrderParam(omsOrderPayParam);
        OmsOrder omsOrder = new OmsOrder();
        AtomicReference<Double> totalAmount = new AtomicReference<>(0.0);
        List<OmsWxAppCart> omsWxAppCarts = omsOrderPayParam.getCarts();
        //优惠信息
        AllCartDiscountDto allCartDiscountDto = orderCombineManager.queryDiscount(omsWxAppCarts, omsOrderPayParam.getMemberId());
        //计算可获得积分
        UmsMember member = umsMemberMapper.selectByPrimaryKey(omsOrderPayParam.getMemberId());
        omsOrder.setMemberId(omsOrderPayParam.getMemberId());
        omsOrder.setCouponId(allCartDiscountDto.getCouponId());
        omsOrder.setOrderSn(String.valueOf(CustomUUID.getInstance(0, 0).generate()));
        omsOrder.setCreateTime(DateUtil.parse(DateUtil.now()));
        omsOrder.setMemberUsername(member.getUsername());
        omsOrder.setTotalAmount(allCartDiscountDto.getAllMoney());
        omsOrder.setPayAmount(allCartDiscountDto.getLastDiscountMoney());
        omsOrder.setPromotionAmount(allCartDiscountDto.getPromotionAmount());
        if (ObjectUtil.isNotNull(allCartDiscountDto.getCouponMoney())) {
            omsOrder.setCouponAmount(allCartDiscountDto.getCouponMoney());
        }
        omsOrder.setPayType(PayTypeEnum.NOPAY.getKey());
        omsOrder.setSourceType(OrderPaySourceEnum.WECHAT.getKey());
        omsOrder.setNote(omsOrderPayParam.getNote());
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
        //更新商品销量，更新商品库存,更新用户积分
        updateGoodByOrder(omsWxAppCarts, member);
        if(ObjectUtil.isNotNull(allCartDiscountDto.getCouponId())) {
            updateCouponOrderHistory(omsOrder);
        }
        return count;
    }

    /**
     * 更新优惠券使用
     * @param omsOrder
     */
    private void updateCouponOrderHistory(OmsOrder omsOrder) {
        SmsCouponHistoryExample smsCouponHistoryExample = new SmsCouponHistoryExample();
        smsCouponHistoryExample.or().andCouponIdEqualTo(omsOrder.getCouponId());
        List<SmsCouponHistory> smsCouponHistories = smsCouponHistoryMapper.selectByExample(smsCouponHistoryExample);
        if(CollectionUtil.isNotEmpty(smsCouponHistories)) {
            SmsCouponHistory smsCouponHistory = smsCouponHistories.get(0);
            smsCouponHistory.setCouponId(omsOrder.getCouponId());
            smsCouponHistory.setUseStatus(CouponStatusEnum.USED.getKey());
            smsCouponHistory.setOrderSn(omsOrder.getOrderSn());
            smsCouponHistory.setMemberNickname(omsOrder.getMemberUsername());
            smsCouponHistoryMapper.updateByPrimaryKeySelective(smsCouponHistory);
        }
    }

    @Override
    public int remarkOrder(MarkOrderPayInfoQuery markOrderPayInfoQuery) {
        //无效订单记录订单状态
        OmsOrder omsOrder = omsOrderMapper.selectByPrimaryKey(markOrderPayInfoQuery.getOrderId());
        omsOrder.setPayAmount(BigDecimal.valueOf(markOrderPayInfoQuery.getPayFee()));
        omsOrder.setStatus(OrderStatusTypeEnum.waitSend.getKey());
        omsOrder.setPayType(markOrderPayInfoQuery.getPayType());
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

    @Override
    public void printOrder(Long orderId) throws Exception {
        //订单
        OmsOrder omsOrder = omsOrderMapper.selectByPrimaryKey(orderId);
        //订单明细
        OmsOrderItemExample itemExample = new OmsOrderItemExample();
        itemExample.or().andOrderIdEqualTo(orderId);
        List<OmsOrderItem> omsOrderItems = omsOrderItemMapper.selectByExample(itemExample);
        //打印对象
        OmsOrderPrintDto omsOrderPrintDto = new OmsOrderPrintDto();
        BeanUtil.copyProperties(omsOrder, omsOrderPrintDto);
        omsOrderPrintDto.setOmsOrderItems(omsOrderItems);
        CustomerTicketPrint customerTicketPrint = new CustomerTicketPrint(omsOrderPrintDto);
        printCustomer.setCustomerTicketPrint(customerTicketPrint);
        printCustomer.PrintCustomer();
    }

    @Override
    public List<OmsOrderDto> listWx(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
       List<OmsOrder> omsOrders = list(queryParam, pageSize, pageNum);
        List<OmsOrderDto> omsOrderDtos = Lists.newArrayList();
       if(CollectionUtil.isNotEmpty(omsOrders)) {
           if(StrUtil.isNotEmpty(queryParam.getKeyWord())) {
               omsOrders = omsOrders.stream().filter(omsOrder ->
                       omsOrder.getOrderSn().equals(queryParam.getKeyWord()) || omsOrder.getReceiverPhone().contains(queryParam.getKeyWord())).collect(Collectors.toList());
           }
           if(CollectionUtil.isNotEmpty(omsOrders)) {
               List<Long> orderIds = omsOrders.stream().map(OmsOrder::getId).collect(Collectors.toList());
               OmsOrderItemExample itemExample = new OmsOrderItemExample();
               itemExample.or().andOrderIdIn(orderIds);
               List<OmsOrderItem> items = omsOrderItemMapper.selectByExample(itemExample);
               if(CollectionUtil.isNotEmpty(items)) {
                   Map<Long,List<OmsOrderItem>> itemMap = items.stream().collect(Collectors.groupingBy(OmsOrderItem::getOrderId));
                   omsOrderDtos = omsOrders.stream().map(omsOrder -> {
                       OmsOrderDto omsOrderDto = BeanCopyUtil.transform(omsOrder, OmsOrderDto.class);
                       List<OmsOrderItem> itemList = itemMap.get(omsOrder.getId());
                       omsOrderDto.setItems(itemList);
                       return omsOrderDto;
                   }).collect(Collectors.toList());
               }
           }
       }
       return omsOrderDtos;
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
