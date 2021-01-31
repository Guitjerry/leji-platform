package com.macro.mall.dto;

import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 所有优惠信息
 */
public class AllCartDiscountDto {
  private List<ProductDiscountDto> discountDtos;
  private List<SmsCoupon> coupons;
  @ApiModelProperty("总金额")
  private Double allMoney;
  @ApiModelProperty("总优惠金额")
  private Double allDiscountMoney;
  @ApiModelProperty("计算后金额")
  private Double lastDiscountMoney;
  @ApiModelProperty("促销优惠金额（满减，折扣的金额）")
  private Double promotionAmount;
  @ApiModelProperty("优惠券金额")
  private Double couponMoney;
  @ApiModelProperty("优惠券id")
  private Long couponId;
  @ApiModelProperty("优惠券名称")
  private String couponName;

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public Long getCouponId() {
    return couponId;
  }

  public void setCouponId(Long couponId) {
    this.couponId = couponId;
  }

  public Double getPromotionAmount() {
    return promotionAmount;
  }

  public void setPromotionAmount(Double promotionAmount) {
    this.promotionAmount = promotionAmount;
  }

  public Double getCouponMoney() {
    return couponMoney;
  }

  public void setCouponMoney(Double couponMoney) {
    this.couponMoney = couponMoney;
  }

  public Double getLastDiscountMoney() {
    return lastDiscountMoney;
  }

  public void setLastDiscountMoney(Double lastDiscountMoney) {
    this.lastDiscountMoney = lastDiscountMoney;
  }

  public Double getAllDiscountMoney() {
    return allDiscountMoney;
  }

  public void setAllDiscountMoney(Double allDiscountMoney) {
    this.allDiscountMoney = allDiscountMoney;
  }

  public Double getAllMoney() {
    return allMoney;
  }

  public void setAllMoney(Double allMoney) {
    this.allMoney = allMoney;
  }

  public List<ProductDiscountDto> getDiscountDtos() {
    return discountDtos;
  }

  public void setDiscountDtos(List<ProductDiscountDto> discountDtos) {
    this.discountDtos = discountDtos;
  }

  public List<SmsCoupon> getCoupons() {
    return coupons;
  }

  public void setCoupons(List<SmsCoupon> coupons) {
    this.coupons = coupons;
  }
}
