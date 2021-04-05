package com.macro.mall.resp;

import com.macro.mall.dto.SmsCouponDto;
import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class SmsCouponResp {
    @ApiModelProperty(value = "可领取")
    private List<SmsCouponDto> availableCoupons;
    @ApiModelProperty(value = "已过期")
    private List<SmsCouponDto> expireCoupons;
    @ApiModelProperty(value = "未使用")
    private List<SmsCouponDto> unUseCoupons;
    @ApiModelProperty(value = "已使用")
    private List<SmsCouponDto> usedCoupons;
}
