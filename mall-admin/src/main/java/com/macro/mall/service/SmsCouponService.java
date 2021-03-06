package com.macro.mall.service;

import com.macro.mall.dto.SmsCouponDto;
import com.macro.mall.dto.SmsCouponParam;
import com.macro.mall.model.SmsCoupon;
import com.macro.mall.resp.SmsCouponResp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 优惠券管理Service
 * Created by macro on 2018/8/28.
 */
public interface SmsCouponService {
    /**
     * 添加优惠券
     */
    @Transactional
    int create(SmsCouponParam couponParam);

    /**
     * 根据优惠券id删除优惠券
     */
    @Transactional
    int delete(Long id);

    /**
     * 根据优惠券id更新优惠券信息
     */
    @Transactional
    int update(Long id, SmsCouponParam couponParam);

    /**
     * 分页获取优惠券列表
     */
    List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * 获取优惠券详情
     * @param id 优惠券表id
     */
    SmsCouponParam getItem(Long id);

    /**
     * 微信查询优惠券
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    SmsCouponResp listWxCoupon(Integer pageNum, Integer pageSize, String token);

    /**
     * 领取优惠券
     * @param memberId
     * @return
     */
    SmsCoupon receiveCoupon(Long memberId, Long couponId) throws Exception;

    /**
     * 我的优惠券
     * @param memberId
     * @return
     */
    SmsCouponResp myCoupon(Long memberId);
}
