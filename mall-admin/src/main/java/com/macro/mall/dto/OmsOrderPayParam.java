package com.macro.mall.dto;

import com.macro.mall.model.OmsCartItem;
import com.macro.mall.model.OmsCompanyAddress;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 下单dto
 * @Date 2021/1/10 17:07
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class OmsOrderPayParam {
    @ApiModelProperty(value = "地址")
    private OmsCompanyAddress addresses;
    @ApiModelProperty(value = "购物车")
    private List<OmsWxAppCart> carts;

    public OmsCompanyAddress getAddresses() {
        return addresses;
    }

    public void setAddresses(OmsCompanyAddress addresses) {
        this.addresses = addresses;
    }

    public List<OmsWxAppCart> getCarts() {
        return carts;
    }

    public void setCarts(List<OmsWxAppCart> carts) {
        this.carts = carts;
    }
}
