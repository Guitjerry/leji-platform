package com.macro.mall.controller.wx;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.service.SmsCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户优惠券管理Controller Created by macro on 2018/8/29.
 */
@Controller
@Api(tags = "UmsCouponController", description = "优惠券信息")
@RequestMapping("/wxApi/coupon")
public class WxCouponController {
  @Autowired
  private SmsCouponService smsCouponService;
  @Autowired
  private UmsMemberMapper memberMapper;

  @ApiOperation("可抢优惠券列表")
  @RequestMapping(value = "/listWx", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult list(@RequestHeader("Authorization") String authorization ,
    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
    String token = authorization.replace("Bearer", "");
    return CommonResult.success(smsCouponService.listWxCoupon(pageNum, pageSize, token));
  }
}
