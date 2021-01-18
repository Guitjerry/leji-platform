package com.macro.mall.mallwechat.service;
import com.macro.mall.mallwechat.dto.SmsHomeRecommendProductDto;
import com.macro.mall.model.SmsHomeRecommendProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页人气推荐管理Service
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeRecommendProductService {
    /**
     * 分页查询推荐
     */
    List<SmsHomeRecommendProductDto> listWx(String productName, Integer recommendStatus);

}
