package com.macro.mall.service;

import com.macro.mall.dto.PmsProductCategoryParam;
import com.macro.mall.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.model.PmsProductCategory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品分类Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductCategoryService {
    /**
     * 创建商品分类
     */
    @Transactional
    PmsProductCategory create(PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 修改商品分类
     */
    @Transactional
    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    /**
     * 分页获取商品分类
     */
    List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 删除商品分类
     */
    int delete(Long id);

    /**
     * 根据ID获取商品分类
     */
    PmsProductCategory getItem(Long id);

    /**
     * 批量修改导航状态
     */
    int updateNavStatus(List<Long> ids, Integer navStatus);

    /**
     * 批量修改显示状态
     */
    int updateShowStatus(List<Long> ids, Integer showStatus);

    /**
     * 以层级形式获取商品分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    List<PmsProductCategory> listCategoryTree();

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

    /**
     * 查询分类下所有品牌
     * @return
     */
    List<PmsBrand> findBrandByCategoryId(Long categotyId);
}
