package com.macro.mall.dto;

import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductFullReduction;
import com.macro.mall.model.PmsProductLadder;
import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;

/**
 * 优惠信息
 */
public class ProductDiscountDto {
  @ApiModelProperty("产品满减")
  private PmsProductFullReduction fullReduction;
  @ApiModelProperty("产品阶梯价格")
  private PmsProductLadder pmsProductLadder;
  @ApiModelProperty("优惠券")
  private SmsCoupon coupon;

  public PmsProductFullReduction getFullReduction() {
    return fullReduction;
  }

  public void setFullReduction(PmsProductFullReduction fullReduction) {
    this.fullReduction = fullReduction;
  }

  public PmsProductLadder getPmsProductLadder() {
    return pmsProductLadder;
  }

  public void setPmsProductLadder(PmsProductLadder pmsProductLadder) {
    this.pmsProductLadder = pmsProductLadder;
  }

  public SmsCoupon getCoupon() {
    return coupon;
  }

  public void setCoupon(SmsCoupon coupon) {
    this.coupon = coupon;
  }
}
