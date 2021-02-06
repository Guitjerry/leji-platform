package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Date 2021/2/6 13:42
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class HomeResultDto {
    @ApiModelProperty("总订单数")
    private Integer allOrder;
    @ApiModelProperty("总销售总额")
    private Double allSale;
    @ApiModelProperty("今日订单总数")
    private Integer todayOrder;
    @ApiModelProperty("今日销售总额")
    private Double saleOrder;
    @ApiModelProperty("待付款订单")
    private Long noPayOrder;
    @ApiModelProperty("待发货订单")
    private Long noSendOrder;
    @ApiModelProperty("已发货订单")
    private Long hasSendOrder;
    @ApiModelProperty("已完成订单")
    private Long hasFinshOrder;
    @ApiModelProperty("库存紧张数量，小于10的为库存紧张")
    private Long stockCount;
    @ApiModelProperty("全部商品数量")
    private Long allCount;

    public Integer getTodayOrder() {
        return todayOrder;
    }

    public void setTodayOrder(Integer todayOrder) {
        this.todayOrder = todayOrder;
    }

    public Double getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(Double saleOrder) {
        this.saleOrder = saleOrder;
    }

    public Long getNoPayOrder() {
        return noPayOrder;
    }

    public void setNoPayOrder(Long noPayOrder) {
        this.noPayOrder = noPayOrder;
    }

    public Long getNoSendOrder() {
        return noSendOrder;
    }

    public void setNoSendOrder(Long noSendOrder) {
        this.noSendOrder = noSendOrder;
    }

    public Long getHasSendOrder() {
        return hasSendOrder;
    }

    public void setHasSendOrder(Long hasSendOrder) {
        this.hasSendOrder = hasSendOrder;
    }

    public Long getHasFinshOrder() {
        return hasFinshOrder;
    }

    public void setHasFinshOrder(Long hasFinshOrder) {
        this.hasFinshOrder = hasFinshOrder;
    }


    public Long getStockCount() {
        return stockCount;
    }

    public void setStockCount(Long stockCount) {
        this.stockCount = stockCount;
    }

    public Long getAllCount() {
        return allCount;
    }

    public void setAllCount(Long allCount) {
        this.allCount = allCount;
    }

    public Integer getAllOrder() {
        return allOrder;
    }

    public void setAllOrder(Integer allOrder) {
        this.allOrder = allOrder;
    }

    public Double getAllSale() {
        return allSale;
    }

    public void setAllSale(Double allSale) {
        this.allSale = allSale;
    }
}
