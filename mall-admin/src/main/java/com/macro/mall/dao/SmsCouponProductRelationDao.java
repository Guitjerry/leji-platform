package com.macro.mall.dao;

import com.macro.mall.dto.SmsCouponDto;
import com.macro.mall.model.SmsCoupon;
import com.macro.mall.model.SmsCouponProductRelation;
import com.macro.mall.resp.SmsCouponResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义优惠券和商品关系关系Dao
 * Created by macro on 2018/8/28.
 */
public interface SmsCouponProductRelationDao {
    /**
     * 批量创建
     */
    int insertList(@Param("list")List<SmsCouponProductRelation> productRelationList);

    List<SmsCouponDto> listAvailableCoupons(Long id);
}
