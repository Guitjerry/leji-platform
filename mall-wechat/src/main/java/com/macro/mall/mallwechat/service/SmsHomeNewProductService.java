package com.macro.mall.mallwechat.service;
import com.macro.mall.mallwechat.dto.SmsHomeNewProductDto;
import com.macro.mall.model.SmsHomeNewProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首页新品管理Service
 * Created by macro on 2018/11/6.
 */
public interface SmsHomeNewProductService {
    /**
     * 微信新品推荐
     * @param productName
     * @param recommendStatus
     * @return
     */
    List<SmsHomeNewProductDto> listWx(String productName, Integer recommendStatus);

}
