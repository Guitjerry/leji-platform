package com.macro.mall.mallwechat.service;
import com.macro.mall.model.PmsBrand;
import java.util.List;

/**
 * 商品品牌Service
 * Created by macro on 2018/4/26.
 */
public interface PmsBrandService {
    /**
     * 获取所有品牌
     */
    List<PmsBrand> listAllBrand();
}
