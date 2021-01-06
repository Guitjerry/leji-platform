package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Date 2021/1/4 18:18
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsHomeNewProductDto {
    private Long id;

    private Long productId;

    private String productName;

    private Integer recommendStatus;

    private Integer sort;

    private Long brandId;

    @ApiModelProperty(value = "品牌")
    private String brandName;

    @ApiModelProperty(value = "图片")
    private String pic;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "市场价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购")
    private Integer promotionType;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "单位")
    private String unit;
}
