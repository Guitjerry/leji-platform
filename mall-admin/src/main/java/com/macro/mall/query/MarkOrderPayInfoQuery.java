package com.macro.mall.query;

/**
 * 记录支付方式与金额
 */
public class MarkOrderPayInfoQuery {
  private Long orderId;
  private Double payFee;
  private Integer payType;

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public Double getPayFee() {
    return payFee;
  }

  public void setPayFee(Double payFee) {
    this.payFee = payFee;
  }

  public Integer getPayType() {
    return payType;
  }

  public void setPayType(Integer payType) {
    this.payType = payType;
  }
}
