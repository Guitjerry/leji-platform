package com.macro.mall.controller.wx;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.common.constant.AuthConstant;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.common.util.TokenUtil;
import com.macro.mall.fegin.OtherFeignIntercept;
import com.macro.mall.mapper.CmsMemberReportMapper;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberExample;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户优惠券管理Controller Created by macro on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberCouponController", description = "用户优惠券管理")
@RequestMapping("/wxApi/coupon")
public class WxMemberCouponController {

  @Autowired
  private OtherFeignIntercept otherFeignIntercept;
  @Autowired
  private UmsMemberMapper memberMapper;

  @ApiOperation("领取指定优惠券")
  @RequestMapping(value = "/send", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult send(Long couponId, Long userId) {
    return otherFeignIntercept.sendCoupon(couponId, userId);
  }

  public UmsMember getWxMember(String token) {
    UmsMember umsMember = null;
    String openId =  TokenUtil.validateToken(token);
    if(StrUtil.isEmpty(openId)){
      Asserts.fail(ResultCode.UNAUTHORIZED);
    }
    UmsMemberExample memberExample = new UmsMemberExample();
    memberExample.or().andOpenIdEqualTo(openId);
    List<UmsMember> umsMembers = memberMapper.selectByExample(memberExample);
    if(CollUtil.isEmpty(umsMembers)) {
      umsMember = umsMembers.get(0);
    }
    return umsMember;
  }
}
