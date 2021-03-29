package com.macro.mall.dto;

import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductFullReduction;
import com.macro.mall.model.SmsCoupon;
import lombok.Data;

import java.util.List;
@Data
public class PmsProductDto extends PmsProduct {
    private List<SmsCoupon> smsCouponList;
    private List<PmsProductFullReduction> fullReductions;
}
