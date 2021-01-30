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
  private List<PmsProductFullReduction> fullReduction;
  @ApiModelProperty("产品阶梯价格")
  private List<PmsProductLadder> pmsProductLadder;
  @ApiModelProperty("优惠信息描述")
  private String discountNote;
  @ApiModelProperty("即将达到的优惠门槛描述")
  private String nearDiscountNote;

  public String getDiscountNote() {
    return discountNote;
  }

  public void setDiscountNote(String discountNote) {
    this.discountNote = discountNote;
  }

  public String getNearDiscountNote() {
    return nearDiscountNote;
  }

  public void setNearDiscountNote(String nearDiscountNote) {
    this.nearDiscountNote = nearDiscountNote;
  }

  public List<PmsProductFullReduction> getFullReduction() {
    return fullReduction;
  }

  public void setFullReduction(List<PmsProductFullReduction> fullReduction) {
    this.fullReduction = fullReduction;
  }

  public List<PmsProductLadder> getPmsProductLadder() {
    return pmsProductLadder;
  }

  public void setPmsProductLadder(List<PmsProductLadder> pmsProductLadder) {
    this.pmsProductLadder = pmsProductLadder;
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
