package com.macro.mall.controller.wx;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.api.ResultCode;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.common.util.TokenUtil;
import com.macro.mall.dto.OmsOrderQueryParam;
import com.macro.mall.fegin.OtherFeignIntercept;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberExample;
import com.macro.mall.query.UmsMemberQuery;
import com.macro.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户优惠券管理Controller Created by macro on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberController", description = "用户管理")
@RequestMapping("/wxApi/member")
public class WxMemberController {
  @Autowired
  private UmsMemberService umsMemberService;
  @Autowired
  private UmsMemberMapper memberMapper;

  @ApiOperation("会员列表")
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult list(UmsMemberQuery umsMemberQuery,
    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
    return CommonResult.success(umsMemberService.list(pageNum, pageSize, umsMemberQuery));
  }
}
