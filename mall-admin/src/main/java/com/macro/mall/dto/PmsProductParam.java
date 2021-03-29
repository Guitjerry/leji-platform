package com.macro.mall.dto;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.macro.mall.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建和修改商品时使用的参数
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsProductParam extends PmsProduct{
    @ApiModelProperty("商品阶梯价格设置")
    private List<PmsProductLadder> productLadderList;
    @ApiModelProperty("商品满减价格设置")
    private List<PmsProductFullReduction> productFullReductionList;
    @ApiModelProperty("商品会员价格设置")
    private List<PmsMemberPrice> memberPriceList;
    @ApiModelProperty("商品的sku库存信息")
    private List<PmsSkuStock> skuStockList;
    @ApiModelProperty("商品参数及自定义规格属性")
    private List<PmsProductAttributeValue> productAttributeValueList;
    @ApiModelProperty("专题和商品关系")
    private List<CmsSubjectProductRelation> subjectProductRelationList;
    @ApiModelProperty("优选专区和商品的关系")
    private List<CmsPrefrenceAreaProductRelation> prefrenceAreaProductRelationList;
   @ApiModelProperty("商品专属优惠券")
    private List<SmsCoupon> couponList;

    public List<PmsProductFullReduction> getProductFullReductionList() {
        if(CollUtil.isEmpty(productFullReductionList)) {
            PmsProductFullReduction pmsProductFullReduction = new PmsProductFullReduction();
            pmsProductFullReduction.setProductId(getId());
            pmsProductFullReduction.setFullPrice(BigDecimal.ZERO);
            pmsProductFullReduction.setReducePrice(BigDecimal.ZERO);
            productFullReductionList = Lists.newArrayList(pmsProductFullReduction);
        }
        return productFullReductionList;
    }

    public List<PmsProductLadder> getProductLadderList() {
        if(CollUtil.isEmpty(productLadderList)) {
            PmsProductLadder pmsProductLadder = new PmsProductLadder();
            pmsProductLadder.setProductId(getId());
            pmsProductLadder.setPrice(BigDecimal.ZERO);
            pmsProductLadder.setDiscount(BigDecimal.ZERO);
            productLadderList = Lists.newArrayList(pmsProductLadder);
        }
        return productLadderList;
    }
}
