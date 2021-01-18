package com.macro.mall.mallwechat.service.impl;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.macro.mall.mallwechat.dao.PmsProductDao;
import com.macro.mall.mallwechat.dto.PmsProductQueryParam;
import com.macro.mall.mallwechat.dto.PmsProductResult;
import com.macro.mall.mallwechat.enums.ListTypeEnum;
import com.macro.mall.mallwechat.service.PmsProductService;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * 商品管理Service实现类
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsProductDao productDao;

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (productQueryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }
        if (productQueryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }
        if (!StringUtils.isEmpty(productQueryParam.getKeyword())) {
            criteria.andNameLike("%" + productQueryParam.getKeyword() + "%");
        }
        if (!StringUtils.isEmpty(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }
        if (productQueryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }
        if (productQueryParam.getProductCategoryId() != null) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }
        return productMapper.selectByExample(productExample);
    }

    @Override
    public List<PmsProduct> listByType(Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<PmsProduct> pmsProducts = Lists.newArrayList();
        if(type.equals(ListTypeEnum.NEW.getKey())) {
            pmsProducts = productMapper.listByNewProduct();
        }else if(type.equals(ListTypeEnum.TEJIA.getKey())) {
            pmsProducts = productMapper.listByTejia();
        }
        return pmsProducts;
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productDao.getUpdateInfo(id);
    }
}
