package com.macro.mall.dto;

import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductFullReduction;
import com.macro.mall.model.PmsProductLadder;
import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 优惠信息
 */
public class ProductDiscountDto {
  private Long goodId;
  private String goodName;
  @ApiModelProperty("产品满减")
  private PmsProductFullReduction fullReduction;
  @ApiModelProperty("产品阶梯价格")
  private PmsProductLadder pmsProductLadder;
  @ApiModelProperty("优惠券")
  private List<SmsCoupon> coupons;
  private Integer type;// 1产品满减  2产品阶梯价格
  private Double saleDiscount;

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Double getSaleDiscount() {
    return saleDiscount;
  }

  public void setSaleDiscount(Double saleDiscount) {
    this.saleDiscount = saleDiscount;
  }

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

  public List<SmsCoupon> getCoupons() {
    return coupons;
  }

  public void setCoupons(List<SmsCoupon> coupons) {
    this.coupons = coupons;
  }

  public Long getGoodId() {
    return goodId;
  }

  public void setGoodId(Long goodId) {
    this.goodId = goodId;
  }

  public String getGoodName() {
    return goodName;
  }

  public void setGoodName(String goodName) {
    this.goodName = goodName;
  }
}
