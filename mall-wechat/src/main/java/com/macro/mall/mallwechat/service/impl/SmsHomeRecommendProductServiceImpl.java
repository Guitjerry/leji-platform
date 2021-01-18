package com.macro.mall.mallwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.macro.mall.mallwechat.constant.CommonConstant;
import com.macro.mall.mallwechat.dto.SmsHomeRecommendProductDto;
import com.macro.mall.mallwechat.service.SmsHomeRecommendProductService;
import com.macro.mall.mapper.PmsProductMapper;
import com.macro.mall.mapper.SmsHomeRecommendProductMapper;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductExample;
import com.macro.mall.model.SmsHomeRecommendProduct;
import com.macro.mall.model.SmsHomeRecommendProductExample;
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
    private List<PmsProduct> findAllPmsProduct(List<Long> productIds) {
        PmsProductExample productExample = new PmsProductExample();
        productExample.or().andIdIn(productIds).andDeleteStatusEqualTo(CommonConstant.FLAG_NO);
        List<PmsProduct> pmsProducts = pmsProductMapper.selectByExample(productExample);
        return pmsProducts;
    }
}
