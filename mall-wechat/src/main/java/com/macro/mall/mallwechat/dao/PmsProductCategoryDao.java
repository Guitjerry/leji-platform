package com.macro.mall.mallwechat.dao;
import com.macro.mall.model.PmsProductCategory;

import java.util.List;

/**
 * 商品分类自定义Dao
 * Created by macro on 2018/5/25.
 */
public interface PmsProductCategoryDao {

    List<PmsProductCategory> listRoot();

    List<PmsProductCategory> listByParentId(Long id);
}
