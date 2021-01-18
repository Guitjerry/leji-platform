package com.macro.mall.mallwechat.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.dto.OmsOrderPayParam;
import com.macro.mall.mallwechat.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单管理Controller
 * Created by macro on 2018/10/11.
 */
@Controller
@Api(tags = "wxOmsOrderController", description = "订单管理")
@RequestMapping("/wxApi/Order")
public class WxOmsOrderController {
    @Autowired
    private OmsOrderService omsOrderService;

    @ApiOperation("保存订单")
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateNote(@RequestBody OmsOrderPayParam omsOrderPayParam) {
        int count = omsOrderService.createOrder(omsOrderPayParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
