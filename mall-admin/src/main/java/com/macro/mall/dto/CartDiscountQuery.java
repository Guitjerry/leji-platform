package com.macro.mall.dto;

import java.util.List;

/**
 * 查询当前订单优惠券所需要参数（包括全场优惠券，分类优惠券，指定商品优惠券）
 */
public class CartDiscountQuery {
  private Long userId;
  private List<Long> productIds;
  private List<Long> categoryIds;

  public List<Long> getCategoryIds() {
    return categoryIds;
  }

  public void setCategoryIds(List<Long> categoryIds) {
    this.categoryIds = categoryIds;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  public void setProductIds(List<Long> productIds) {
    this.productIds = productIds;
  }
}
