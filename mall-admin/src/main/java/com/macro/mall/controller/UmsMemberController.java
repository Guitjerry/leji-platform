package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.query.UmsMemberQuery;
import com.macro.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户优惠券管理Controller Created by macro on 2018/8/29.
 */
@Controller
@Api(tags = "UmsMemberController", description = "用户管理")
@RequestMapping("/member")
public class UmsMemberController {
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

  @ApiOperation("设置店长")
  @RequestMapping(value = "/configAdmin", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult configAdmin(Long memberId) {
    umsMemberService.configAdmin(memberId);
    return CommonResult.success("ok");
  }


}
