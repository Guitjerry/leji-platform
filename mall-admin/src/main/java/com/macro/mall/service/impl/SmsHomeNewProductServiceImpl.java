package com.macro.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dto.SmsHomeNewProductDto;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.mapper.SmsHomeNewProductMapper;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductExample;
import com.macro.mall.model.SmsHomeNewProduct;
import com.macro.mall.model.SmsHomeNewProductExample;
import com.macro.mall.service.SmsHomeNewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页新品推荐管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {
    @Autowired
    private SmsHomeNewProductMapper homeNewProductMapper;
    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Override
    public int create(List<SmsHomeNewProduct> homeNewProductList) {
        for (SmsHomeNewProduct SmsHomeNewProduct : homeNewProductList) {
            SmsHomeNewProduct.setRecommendStatus(1);
            SmsHomeNewProduct.setSort(0);
            homeNewProductMapper.insert(SmsHomeNewProduct);
        }
        return homeNewProductList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeNewProduct homeNewProduct = new SmsHomeNewProduct();
        homeNewProduct.setId(id);
        homeNewProduct.setSort(sort);
        return homeNewProductMapper.updateByPrimaryKeySelective(homeNewProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeNewProductExample example = new SmsHomeNewProductExample();
        example.createCriteria().andIdIn(ids);
        return homeNewProductMapper.deleteByExample(example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeNewProductExample example = new SmsHomeNewProductExample();
        example.createCriteria().andIdIn(ids);
        SmsHomeNewProduct record = new SmsHomeNewProduct();
        record.setRecommendStatus(recommendStatus);
        return homeNewProductMapper.updateByExampleSelective(record,example);
    }

    private List<PmsProduct> findAllPmsProduct(List<Long> productIds) {
        PmsProductExample productExample = new PmsProductExample();
        productExample.or().andIdIn(productIds).andDeleteStatusEqualTo(CommonConstant.FLAG_NO);
        List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(productExample);
        return pmsProducts;
    }

    @Override
    public List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        SmsHomeNewProductExample example = new SmsHomeNewProductExample();
        SmsHomeNewProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(productName)){
            criteria.andProductNameLike("%"+productName+"%");
        }
        if(recommendStatus!=null){
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        example.setOrderByClause("sort desc");

        return homeNewProductMapper.selectByExample(example);
    }

    @Override
    public List<SmsHomeNewProductDto> listWx(String productName, Integer recommendStatus) {
        SmsHomeNewProductExample example = new SmsHomeNewProductExample();
        SmsHomeNewProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(productName)){
            criteria.andProductNameLike("%"+productName+"%");
        }
        if(recommendStatus!=null){
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        example.setOrderByClause("sort desc");

        List<SmsHomeNewProduct> smsHomeNewProducts = homeNewProductMapper.selectByExample(example);
        List<SmsHomeNewProductDto> resultHomeNewProductDto = Lists.newArrayList();
        //新品，需要获取商品额外属性
        if(CollectionUtil.isNotEmpty(smsHomeNewProducts)) {
            List<Long> productIds = smsHomeNewProducts.stream().map(SmsHomeNewProduct::getProductId).collect(Collectors.toList());
            List<PmsProduct> pmsProducts = findAllPmsProduct(productIds);
            Map<Long,PmsProduct> pmsProductMap =
                    pmsProducts.stream().collect(Collectors.toMap(PmsProduct::getId, Function.identity()));

            resultHomeNewProductDto =  smsHomeNewProducts.stream().map(smsHomeNewProduct -> {
                PmsProduct pmsProduct = pmsProductMap.get(smsHomeNewProduct.getProductId());
                SmsHomeNewProductDto smsHomeNewProductDto = new SmsHomeNewProductDto();
                BeanUtil.copyProperties(smsHomeNewProduct, smsHomeNewProductDto);

                if(ObjectUtil.isNotNull(pmsProduct)) {
                    BeanUtil.copyProperties(pmsProduct, smsHomeNewProductDto);
                }
                return smsHomeNewProductDto;

            }).collect(Collectors.toList());
        }

        return resultHomeNewProductDto;
    }
}
