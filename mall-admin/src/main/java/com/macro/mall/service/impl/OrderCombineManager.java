package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.macro.mall.common.enums.CouponTypeEnum;
import com.macro.mall.dao.PmsProductDao;
import com.macro.mall.dto.AllCartDiscountDto;
import com.macro.mall.dto.CartDiscountQuery;
import com.macro.mall.dto.OmsWxAppCart;
import com.macro.mall.dto.ProductDiscountDto;
import com.macro.mall.enums.CouponDiscountEnum;
import com.macro.mall.mapper.PmsProductFullReductionMapper;
import com.macro.mall.mapper.PmsProductLadderMapper;
import com.macro.mall.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Date 2021/1/31 9:13
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记 下单用到的通用方法管理器
 */
@Component
public class OrderCombineManager {

  @Autowired
  private PmsProductLadderMapper productLadderMapper;

  @Autowired
  private PmsProductFullReductionMapper productFullReductionMapper;

  @Autowired
  private PmsProductDao productDao;

  private List<PmsProductLadder> getPmsProductLadder(List<Long> productIds) {
    PmsProductLadderExample ladderExample = new PmsProductLadderExample();
    ladderExample.or().andProductIdIn(productIds);
    List<PmsProductLadder> ladders = productLadderMapper.selectByExample(ladderExample);
    return ladders;
  }

  private List<PmsProductFullReduction> getPmsProductFullReduction(List<Long> productIds) {
    PmsProductFullReductionExample reductionExample = new PmsProductFullReductionExample();
    reductionExample.or().andProductIdIn(productIds);
    List<PmsProductFullReduction> fullReductions = productFullReductionMapper.selectByExample(reductionExample);
    return fullReductions;
  }

  private List<SmsCoupon> getCoupon(Long memberId, List<Long> productIds, List<Long> categoryIds) {
    CartDiscountQuery cartDiscountQuery = new CartDiscountQuery();
    cartDiscountQuery.setUserId(memberId);
    cartDiscountQuery.setProductIds(productIds);
    cartDiscountQuery.setCategoryIds(categoryIds);
    return productDao.queryCartMemberCoupon(cartDiscountQuery);
  }

  public AllCartDiscountDto queryDiscount(List<OmsWxAppCart> carts, Long memberId) {
    AllCartDiscountDto allCartDiscountDto = new AllCartDiscountDto();
    List<ProductDiscountDto> discountDtos = Lists.newArrayList();

    BigDecimal allMoney = BigDecimal.ZERO;//总价格
    BigDecimal allDiscountMoney = BigDecimal.ZERO;//总优惠金额
    BigDecimal promotionAmount = BigDecimal.ZERO;;//促销优惠金额
    BigDecimal couponMoney = BigDecimal.ZERO;;//优惠券金额
    //计算优惠信息
    if (!CollUtil.isEmpty(carts)) {
      //某项商品总金额
      Double allPrice = carts.stream().mapToDouble(cart -> cart.getPrice() * cart.getCount()).sum();
      allMoney = BigDecimal.valueOf(allPrice).add(allMoney);
      Map<Long, List<OmsWxAppCart>> catrgoryCart =
        carts.stream().collect(Collectors.groupingBy(OmsWxAppCart::getProductCategoryId));
      Map<Long, OmsWxAppCart> goodCart =
        carts.stream().collect(Collectors.toMap(OmsWxAppCart::getId, Function.identity()));

      //产品阶梯价格
      List<Long> productIds = carts.stream().map(OmsWxAppCart::getId).collect(Collectors.toList());
      List<PmsProductLadder> ladders = getPmsProductLadder(productIds);
      Map<Long, List<PmsProductLadder>> ladderMap =
        CollUtil.isNotEmpty(ladders) ? ladders.stream().collect(Collectors.groupingBy(PmsProductLadder::getProductId))
          : null;

      //产品满减价格
      List<PmsProductFullReduction> fullReductions = getPmsProductFullReduction(productIds);
      Map<Long, List<PmsProductFullReduction>> reductionMap =
        CollUtil.isNotEmpty(fullReductions) ? fullReductions.stream()
          .collect(Collectors.groupingBy(PmsProductFullReduction::getProductId)) : null;

      //查询可以使用的优惠券
      List<Long> categoryIds = carts.stream().map(OmsWxAppCart::getProductCategoryId).collect(Collectors.toList());
      List<SmsCoupon> smsCoupons = getCoupon(memberId, productIds, categoryIds);

      List<SmsCoupon> smsFilterCoupons = Lists.newArrayList();
      //总金额
      for (SmsCoupon smsCoupon : smsCoupons) {
        //如果优惠券是通用的，比较总金额与优惠券限定金额
        if (smsCoupon.getUseType().equals(CouponTypeEnum.COMMON.getKey())
          && smsCoupon.getMinPoint().doubleValue() >= allPrice) {
          smsFilterCoupons.add(smsCoupon);
        } else if (smsCoupon.getUseType().equals(CouponTypeEnum.CATEGORY.getKey())) {
          //如果优惠券是某个分类的，比较分类商品总金额与优惠券限定金额
          List<OmsWxAppCart> omsWxAppCarts = catrgoryCart.get(smsCoupon.getCid());
          Double allCatoryPrice = omsWxAppCarts.stream().mapToDouble(cart -> cart.getPrice() * cart.getCount()).sum();
          if (smsCoupon.getMinPoint().doubleValue() >= allCatoryPrice) {
            smsFilterCoupons.add(smsCoupon);
          }
        } else if (smsCoupon.getUseType().equals(CouponTypeEnum.GOOD.getKey())) {
          //如果优惠券是某个商品的，比较商品总金额与优惠券限定金额
          OmsWxAppCart omsWxAppCart = goodCart.get(smsCoupon.getCid());
          Double goodPrice = omsWxAppCart.getPrice() * omsWxAppCart.getCount();
          if (smsCoupon.getMinPoint().doubleValue() <= goodPrice) {
            smsFilterCoupons.add(smsCoupon);
          }
        }
      }
      smsFilterCoupons = smsFilterCoupons.stream().sorted((o1, o2) -> o1.getAmount().compareTo(o2.getAmount()))
        .collect(Collectors.toList());
      if (CollectionUtil.isNotEmpty(smsFilterCoupons)) {
        allCartDiscountDto.setCoupons(Lists.newArrayList(smsFilterCoupons.get(0)));
        allDiscountMoney = allDiscountMoney.add(smsFilterCoupons.get(0).getAmount());
        allCartDiscountDto.setDiscountType(CouponDiscountEnum.COUPON.getKey());
        couponMoney = couponMoney.add(smsFilterCoupons.get(0).getAmount());
        allCartDiscountDto.setCouponMoney(couponMoney);
        allCartDiscountDto.setCouponId(smsFilterCoupons.get(0).getId());
        allCartDiscountDto.setCouponName(smsFilterCoupons.get(0).getName());
      }

      //满减力度最大的
      for (OmsWxAppCart omsWxAppCart : carts) {
        ProductDiscountDto productDiscountDto = new ProductDiscountDto();
        productDiscountDto.setGoodId(omsWxAppCart.getId());
        productDiscountDto.setGoodName(omsWxAppCart.getName());
        BigDecimal goodAllPrice = BigDecimal.valueOf(omsWxAppCart.getPrice() * omsWxAppCart.getCount());
        if (ObjectUtil.isNotNull(reductionMap)) {
          //满减
          List<PmsProductFullReduction> reductions = reductionMap.get(omsWxAppCart.getId());
          if(CollectionUtil.isNotEmpty(reductions)) {
            reductions =  reductions.stream()
                    .filter(pmsProductFullReduction -> pmsProductFullReduction.getFullPrice().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            reductions = reductions.stream()
                    .filter(pmsProductFullReduction -> pmsProductFullReduction.getFullPrice().compareTo(goodAllPrice) <= 0)
                    .sorted((o1, o2) -> o2.getFullPrice().compareTo(o1.getFullPrice())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(reductions)) {
              productDiscountDto.setFullReduction(reductions);
              PmsProductFullReduction fullReduction = reductions.get(0);

              if (allDiscountMoney.compareTo(fullReduction.getReducePrice()) < 0) {
                allDiscountMoney = fullReduction.getReducePrice();
                allCartDiscountDto.setDiscountType(CouponDiscountEnum.MAN.getKey());
              }
              promotionAmount = promotionAmount.add(fullReduction.getReducePrice());
              String discountNote =
                      omsWxAppCart.getName() + "【金额满" + fullReduction.getFullPrice() + "元减" + fullReduction.getReducePrice()
                              + "】";
              productDiscountDto.setDiscountNote(discountNote);
            }
          }

        }

        //阶梯折扣
        if (ObjectUtil.isNotNull(ladderMap)) {
          List<PmsProductLadder> pmsProductLadders = ladderMap.get(omsWxAppCart.getId());

          if (CollectionUtil.isNotEmpty(pmsProductLadders)) {
            pmsProductLadders =  pmsProductLadders.stream()
                    .filter(pmsProductLadder -> pmsProductLadder.getPrice().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());
            pmsProductLadders = pmsProductLadders.stream()
                    .filter(pmsProductLadder -> omsWxAppCart.getCount() >= pmsProductLadder.getCount())
                    .sorted((o1, o2) -> o1.getDiscount().compareTo(o2.getDiscount())).collect(Collectors.toList());
            productDiscountDto.setPmsProductLadder(pmsProductLadders);
            if(CollUtil.isNotEmpty(pmsProductLadders)) {
              PmsProductLadder ladder = pmsProductLadders.get(0);
              //优惠金额
              BigDecimal saleDiscount = BigDecimal.valueOf(100.00 - ladder.getDiscount().doubleValue());
              BigDecimal saleZhe = saleDiscount.divide(new BigDecimal("100.00"));
              BigDecimal discountMoney = goodAllPrice.multiply(saleZhe);
              if (allDiscountMoney.compareTo(discountMoney) < 0) {
                allDiscountMoney = discountMoney;
                allCartDiscountDto.setDiscountType(CouponDiscountEnum.ZHE.getKey());
              }
              ladder.setDiscountDesc(String.valueOf(discountMoney.setScale(0, BigDecimal.ROUND_HALF_UP)));
              String discountNote =
                      omsWxAppCart.getName() + "【数量满" + ladder.getCount() + "立享" + ladder.getDiscount() + "折】";
              productDiscountDto.setDiscountNote(discountNote);
            }

          }

        }

        discountDtos.add(productDiscountDto);
      }

    }
    BigDecimal lastDiscountMoney = allMoney.subtract(allDiscountMoney);
    allCartDiscountDto.setDiscountDtos(discountDtos);
    allCartDiscountDto.setAllMoney(allMoney);
    allCartDiscountDto.setAllDiscountMoney(allDiscountMoney);
    allCartDiscountDto.setPayAmount(allMoney.subtract(allDiscountMoney));
    allCartDiscountDto.setPromotionAmount(allDiscountMoney);
    allCartDiscountDto.setLastDiscountMoney(lastDiscountMoney);
    return allCartDiscountDto;
  }
}
