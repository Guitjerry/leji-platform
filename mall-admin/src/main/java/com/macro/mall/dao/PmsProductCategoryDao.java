package com.macro.mall.dao;

import com.macro.mall.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.model.PmsProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类自定义Dao
 * Created by macro on 2018/5/25.
 */
public interface PmsProductCategoryDao {
    /**
     * 获取商品分类及其子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    List<PmsProductCategory> listRoot();

    List<PmsProductCategory> listByParentId(Long id);

    List<PmsBrand> findBrandByCategoryId(@Param(value = "categotyId") Long categotyId);
}
