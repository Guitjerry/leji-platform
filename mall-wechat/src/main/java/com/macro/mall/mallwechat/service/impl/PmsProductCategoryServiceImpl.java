package com.macro.mall.mallwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.mallwechat.constant.CommonConstant;
import com.macro.mall.mallwechat.dao.PmsProductCategoryDao;
import com.macro.mall.mallwechat.service.PmsProductCategoryService;
import com.macro.mall.mapper.PmsProductCategoryMapper;
import com.macro.mall.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * PmsProductCategoryService实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private PmsProductCategoryDao productCategoryDao;

    @Override
    public List<PmsProductCategory> getListAll() {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.or().andParentIdEqualTo(0L).andNavStatusEqualTo(CommonConstant.FLAG_YES);
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> listCategoryByParentId(Long parentId) {
        PmsProductCategoryExample categoryExample = new PmsProductCategoryExample();
        categoryExample.or().andParentIdEqualTo(parentId);
        return productCategoryMapper.selectByExample(categoryExample);
    }

    @Override
    public List<PmsProductCategory> listCategoryTree() {
        List<PmsProductCategory> rootCategory = productCategoryDao.listRoot();
        return listCategoryTree(rootCategory);
    }

    private List<PmsProductCategory> listCategoryTree(List<PmsProductCategory> rootCategorys) {
        if(CollectionUtil.isNotEmpty(rootCategorys)) {
            rootCategorys.forEach(pmsProductCategory -> {
                List<PmsProductCategory> childrenCategorys =  productCategoryDao.listByParentId(pmsProductCategory.getId());
                if(CollectionUtil.isNotEmpty(childrenCategorys)) {
                    pmsProductCategory.setChildren(childrenCategorys);
                    listCategoryTree(childrenCategorys);
                }
            });
        }
        return rootCategorys;
    }
}
