package com.macro.mall.dto;

import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderItem;
import lombok.Data;

import java.util.List;
@Data
public class OmsOrderDto extends OmsOrder {
    private List<OmsOrderItem> items;
    private String couponName;
}
