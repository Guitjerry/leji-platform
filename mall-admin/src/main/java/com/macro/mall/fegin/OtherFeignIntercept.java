package com.macro.mall.fegin;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mall-portal")
public interface OtherFeignIntercept {

    /**
     * 获取用户信息
     */
    @RequestMapping(value = "/member/coupon/add", method = RequestMethod.GET)
    CommonResult sendCoupon(@RequestParam(value = "couponId") Long couponId,@RequestParam(value = "userId") Long userId);
}
