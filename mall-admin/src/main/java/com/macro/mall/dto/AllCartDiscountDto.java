package com.macro.mall.dto;

import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 所有优惠信息
 */
@Data
public class AllCartDiscountDto {
  private List<ProductDiscountDto> discountDtos;
  private List<SmsCoupon> coupons;
  @ApiModelProperty("总金额")
  private BigDecimal allMoney;
  @ApiModelProperty("总优惠金额")
  private BigDecimal allDiscountMoney;
  @ApiModelProperty("计算后金额")
  private BigDecimal lastDiscountMoney;
  @ApiModelProperty("促销优惠金额（满减，折扣的金额）")
  private BigDecimal promotionAmount;
  @ApiModelProperty("优惠券金额")
  private BigDecimal couponMoney;
  @ApiModelProperty("优惠券id")
  private Long couponId;
  @ApiModelProperty("优惠券名称")
  private String couponName;
  @ApiModelProperty("支付金额")
  private BigDecimal payAmount;
  @ApiModelProperty("优惠类型 1满减 2折扣 3优惠券")
  private Integer discountType;
}
