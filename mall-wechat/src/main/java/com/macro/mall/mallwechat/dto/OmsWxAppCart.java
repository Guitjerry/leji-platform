package com.macro.mall.mallwechat.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Date 2021/1/11 21:14
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class OmsWxAppCart {
    @ApiModelProperty(value = "商品Id")
    private Integer id;
    @ApiModelProperty(value = "品牌id")
    private Integer brandId;
    @ApiModelProperty(value = "产品分类")
    private Integer productCategoryId;
    @ApiModelProperty(value = "产品名称")
    private String name;
    @ApiModelProperty(value = "产品图片")
    private String pic;
    @ApiModelProperty(value = "产品价格")
    private Double price;
    @ApiModelProperty(value = "产品副标题")
    private String subTitle;
    @ApiModelProperty(value = "产品库存")
    private Integer stock;
    @ApiModelProperty(value = "产品品牌名称")
    private String brandName;
    @ApiModelProperty(value = "产品分类名称")
    private String productCategoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }
}
