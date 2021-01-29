package com.macro.mall.dto;

import com.macro.mall.model.SmsCoupon;
import java.util.List;

/**
 * 所有优惠信息
 */
public class AllCartDiscountDto {
  private List<ProductDiscountDto> discountDtos;
  private List<SmsCoupon> coupons;

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
