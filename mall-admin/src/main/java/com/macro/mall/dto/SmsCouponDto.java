package com.macro.mall.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.macro.mall.model.SmsCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SmsCouponDto extends SmsCoupon {
    private Long id;
    private String brandName;
    @ApiModelProperty(value = "可领券数量")
    private Integer perLimit;
    private String productCategoryName;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "即将过期 0否 1是")
    private Integer nearExpire;
}
