package com.macro.mall.mallwechat.service;
import com.macro.mall.mallwechat.dto.SmsHomeBrandDto;
import com.macro.mall.model.SmsHomeBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页品牌管理Service
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeBrandService {
    /**
     * 查询品牌推荐
     */
    List<SmsHomeBrandDto> listWx(String brandName, Integer recommendStatus);
}
