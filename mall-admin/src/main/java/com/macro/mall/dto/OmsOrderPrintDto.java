package com.macro.mall.dto;

import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderItem;

import java.util.List;

/**
 * @Date 2021/2/6 20:11
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class OmsOrderPrintDto extends OmsOrder{
    private List<OmsOrderItem> omsOrderItems;

    public List<OmsOrderItem> getOmsOrderItems() {
        return omsOrderItems;
    }

    public void setOmsOrderItems(List<OmsOrderItem> omsOrderItems) {
        this.omsOrderItems = omsOrderItems;
    }
}
