package com.macro.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dto.SmsHomeNewProductDto;
import com.macro.mall.dto.SmsHomeRecommendProductDto;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.mapper.SmsHomeRecommendProductMapper;
import com.macro.mall.model.*;
import com.macro.mall.service.SmsHomeRecommendProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页人气推荐管理Service实现类
 * Created by macro on 2018/11/7.
 */
@Service
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {
    @Autowired
    private SmsHomeRecommendProductMapper recommendProductMapper;
    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public int create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
            recommendProductMapper.insert(recommendProduct);
        }
        return homeRecommendProductList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return recommendProductMapper.updateByPrimaryKeySelective(recommendProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        example.createCriteria().andIdIn(ids);
        return recommendProductMapper.deleteByExample(example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        example.createCriteria().andIdIn(ids);
        SmsHomeRecommendProduct record = new SmsHomeRecommendProduct();
        record.setRecommendStatus(recommendStatus);
        return recommendProductMapper.updateByExampleSelective(record,example);
    }

    private List<PmsProduct> findAllPmsProduct(List<Long> productIds) {
        PmsProductExample productExample = new PmsProductExample();
        productExample.or().andIdIn(productIds).andDeleteStatusEqualTo(CommonConstant.FLAG_NO);
        List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(productExample);
        return pmsProducts;
    }

    @Override
    public List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        SmsHomeRecommendProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(productName)){
            criteria.andProductNameLike("%"+productName+"%");
        }
        if(recommendStatus!=null){
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        example.setOrderByClause("sort desc");
        return recommendProductMapper.selectByExample(example);
    }

    @Override
    public List<SmsHomeRecommendProductDto> listWx(String productName, Integer recommendStatus) {
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        SmsHomeRecommendProductExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(productName)){
            criteria.andProductNameLike("%"+productName+"%");
        }
        if(recommendStatus!=null){
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        example.setOrderByClause("sort desc");
        List<SmsHomeRecommendProduct> smsHomeRecommendProducts = recommendProductMapper.selectByExample(example);
        List<SmsHomeRecommendProductDto> resultHomeRecommendProductDto = Lists.newArrayList();
        //新品，需要获取商品额外属性
        if(CollectionUtil.isNotEmpty(smsHomeRecommendProducts)) {
            List<Long> productIds = smsHomeRecommendProducts.stream().map(SmsHomeRecommendProduct::getProductId).collect(Collectors.toList());
            List<PmsProduct> pmsProducts = findAllPmsProduct(productIds);
            Map<Long,PmsProduct> pmsProductMap =
                    pmsProducts.stream().collect(Collectors.toMap(PmsProduct::getId, Function.identity()));

            resultHomeRecommendProductDto =  smsHomeRecommendProducts.stream().map(smsHomeRecommendProduct -> {
                PmsProduct pmsProduct = pmsProductMap.get(smsHomeRecommendProduct.getProductId());
                SmsHomeRecommendProductDto smsHomeRecommendProductDto = new SmsHomeRecommendProductDto();
                BeanUtil.copyProperties(smsHomeRecommendProduct, smsHomeRecommendProductDto);

                if(ObjectUtil.isNotNull(pmsProduct)) {
                    BeanUtil.copyProperties(pmsProduct, smsHomeRecommendProductDto);
                }
                return smsHomeRecommendProductDto;

            }).collect(Collectors.toList());
        }
        return resultHomeRecommendProductDto;
    }
}
