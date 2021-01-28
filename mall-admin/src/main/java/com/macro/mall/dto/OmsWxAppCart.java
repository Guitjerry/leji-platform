package com.macro.mall.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Date 2021/1/11 21:14
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
public class OmsWxAppCart {
    @ApiModelProperty(value = "商品Id")
    private Long id;
    @ApiModelProperty(value = "品牌id")
    private Long brandId;
    @ApiModelProperty(value = "产品分类")
    private Long productCategoryId;
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

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }

  public Long getProductCategoryId() {
    return productCategoryId;
  }

  public void setProductCategoryId(Long productCategoryId) {
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
