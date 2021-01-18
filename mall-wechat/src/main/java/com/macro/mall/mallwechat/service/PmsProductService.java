package com.macro.mall.mallwechat.service;
import com.macro.mall.mallwechat.dto.PmsProductQueryParam;
import com.macro.mall.mallwechat.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;

import java.util.List;

/**
 * 商品管理Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductService {
    /**
     * 根据商品名称或者货号模糊查询
     */
    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);
    List<PmsProduct> listByType(Integer type, Integer pageSize, Integer pageNum);
    PmsProductResult getUpdateInfo(Long id);
}
