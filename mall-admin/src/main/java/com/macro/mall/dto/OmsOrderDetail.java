package com.macro.mall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.OmsOrderItem;
import com.macro.mall.model.OmsOrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 订单详情信息
 * Created by macro on 2018/10/11.
 */
@Data
public class OmsOrderDetail extends OmsOrder {
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;
    @ApiModelProperty("订单操作记录列表")
    private List<OmsOrderOperateHistory> historyList;
    private Integer allCount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
