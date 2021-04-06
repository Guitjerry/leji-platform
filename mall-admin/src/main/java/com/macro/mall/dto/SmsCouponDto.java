package com.macro.mall.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.macro.mall.model.SmsCoupon;
import lombok.Data;

import java.util.Date;

@Data
public class SmsCouponDto extends SmsCoupon {
    private String brandName;
    private String productCategoryName;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;
}