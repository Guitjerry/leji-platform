package com.macro.mall.mallwechat.service;
import com.macro.mall.model.PmsProductCategory;

import java.util.List;

/**
 * 商品分类Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductCategoryService {
    /**
     * 小程序查询分类列表
     * @return
     */
    List<PmsProductCategory> getListAll();

    /**
     * 查询子菜单
     * @return
     */
    List<PmsProductCategory> listCategoryByParentId(Long parentId);

    List<PmsProductCategory> listCategoryTree();
}
