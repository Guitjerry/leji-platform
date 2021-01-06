package com.macro.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dto.SmsHomeBrandDto;
import com.macro.mall.mapper.PmsBrandMapper;
import com.macro.mall.mapper.SmsHomeBrandMapper;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.model.PmsBrandExample;
import com.macro.mall.model.SmsHomeBrand;
import com.macro.mall.model.SmsHomeBrandExample;
import com.macro.mall.service.SmsHomeBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页品牌管理Service实现类
 * Created by macro on 2018/11/6.
 */
@Service
public class SmsHomeBrandServiceImpl implements SmsHomeBrandService {
    @Autowired
    private SmsHomeBrandMapper homeBrandMapper;
    @Autowired
    private PmsBrandMapper pmsBrandMapper;
    @Override
    public int create(List<SmsHomeBrand> homeBrandList) {
        for (SmsHomeBrand smsHomeBrand : homeBrandList) {
            smsHomeBrand.setRecommendStatus(1);
            smsHomeBrand.setSort(0);
            homeBrandMapper.insert(smsHomeBrand);
        }
        return homeBrandList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeBrand homeBrand = new SmsHomeBrand();
        homeBrand.setId(id);
        homeBrand.setSort(sort);
        return homeBrandMapper.updateByPrimaryKeySelective(homeBrand);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeBrandExample example = new SmsHomeBrandExample();
        example.createCriteria().andIdIn(ids);
        return homeBrandMapper.deleteByExample(example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeBrandExample example = new SmsHomeBrandExample();
        example.createCriteria().andIdIn(ids);
        SmsHomeBrand record = new SmsHomeBrand();
        record.setRecommendStatus(recommendStatus);
        return homeBrandMapper.updateByExampleSelective(record,example);
    }

    private List<PmsBrand> findPmsBrand(List<Long> brandIds) {
        PmsBrandExample brandExample = new PmsBrandExample();
        brandExample.or().andIdIn(brandIds).andShowStatusEqualTo(CommonConstant.FLAG_YES);
        return pmsBrandMapper.selectByExample(brandExample);
    }

    @Override
    public List<SmsHomeBrandDto> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        SmsHomeBrandExample example = new SmsHomeBrandExample();
        SmsHomeBrandExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(brandName)){
            criteria.andBrandNameLike("%"+brandName+"%");
        }
        if(recommendStatus!=null){
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        example.setOrderByClause("sort desc");

        //品牌推荐
        List<SmsHomeBrand> smsHomeBrands = homeBrandMapper.selectByExample(example);
        List<SmsHomeBrandDto> brandDtos = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(smsHomeBrands)) {
            List<Long> brandIds = smsHomeBrands.stream().map(SmsHomeBrand::getBrandId).collect(Collectors.toList());
            List<PmsBrand> pmsBrands = findPmsBrand(brandIds);

            //写入图标
            if(CollectionUtil.isNotEmpty(pmsBrands)) {
                Map<Long, PmsBrand> pmsBrandMap = pmsBrands.stream().collect(Collectors.toMap(PmsBrand::getId, Function.identity()));
                brandDtos =  smsHomeBrands.stream().map(smsHomeBrand -> {
                    SmsHomeBrandDto smsHomeBrandDto = new SmsHomeBrandDto();
                    BeanUtil.copyProperties(smsHomeBrand, smsHomeBrandDto);
                    if(pmsBrandMap.containsKey(smsHomeBrand.getBrandId())) {
                        PmsBrand pmsBrand = pmsBrandMap.get(smsHomeBrand.getBrandId());
                        smsHomeBrandDto.setLogo(pmsBrand.getLogo());
                        smsHomeBrandDto.setRecommend(pmsBrand.getRecommend());
                    }
                    return smsHomeBrandDto;
                }).collect(Collectors.toList());
            }
        }
        return brandDtos;
    }
}
