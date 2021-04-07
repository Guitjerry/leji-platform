package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class SmsHomeRecommendProductDto implements Serializable {
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
    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购")
    private Integer promotionType;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "单位")
    private String unit;

    private Integer isShow;
    private static final long serialVersionUID = 1L;
}