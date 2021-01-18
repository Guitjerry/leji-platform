package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.*;
import com.macro.mall.model.OmsOrder;
import com.macro.mall.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
