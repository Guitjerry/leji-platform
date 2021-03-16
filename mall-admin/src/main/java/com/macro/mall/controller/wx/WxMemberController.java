package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.query.UmsMemberQuery;
import com.macro.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

  @ApiOperation("查询会员信息")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult getMember(@PathVariable Long id) {
    return CommonResult.success(umsMemberService.getById(id));
  }

  @ApiOperation("更新会员")
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult updateMember(@RequestBody UmsMember umsMember) {
    umsMemberService.update(umsMember);
    return CommonResult.success("ok");
  }
}
