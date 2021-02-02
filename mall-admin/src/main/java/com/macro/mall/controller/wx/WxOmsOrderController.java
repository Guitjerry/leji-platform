package com.macro.mall.controller.wx;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.*;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.model.UmsMember;
import com.macro.mall.service.CmsSubjectService;
import com.macro.mall.service.OmsOrderService;
import com.macro.mall.service.impl.OrderCombineManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 订单管理Controller Created by macro on 2018/10/11.
 */
@Controller
@Api(tags = "wxOmsOrderController", description = "订单管理")
@RequestMapping("/wxApi/Order")
public class WxOmsOrderController {

  @Autowired
  private OmsOrderService omsOrderService;
  @Autowired
  private OmsOrderService orderService;

  @ApiOperation("保存订单")
  @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult saveOrder(@RequestBody OmsOrderPayParam omsOrderPayParam) {
    int count = omsOrderService.createOrder(omsOrderPayParam);
    if (count > 0) {
      return CommonResult.success(count);
    }
    return CommonResult.failed();
  }

  @ApiOperation("查询订单")
  @RequestMapping(value = "/list", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<OmsOrder>> list(OmsOrderQueryParam queryParam,
    @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
    List<OmsOrder> orderList = orderService.list(queryParam, pageSize, pageNum);
    return CommonResult.success(CommonPage.restPage(orderList));
  }

}
