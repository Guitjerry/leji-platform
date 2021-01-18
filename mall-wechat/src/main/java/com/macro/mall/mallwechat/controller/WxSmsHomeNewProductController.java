package com.macro.mall.mallwechat.controller;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.dto.SmsHomeNewProductDto;
import com.macro.mall.mallwechat.service.SmsHomeNewProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 首页新品管理Controller
 * Created by macro on 2018/11/6.
 */
@Controller
@Api(tags = "WxSmsHomeNewProductController", description = "首页新品管理")
@RequestMapping("/wxApi/home/newProduct")
public class WxSmsHomeNewProductController {
    @Autowired
    private SmsHomeNewProductService homeNewProductService;
    @ApiOperation("查询新品推荐")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsHomeNewProductDto>> list(@RequestParam(value = "productName", required = false) String productName,
                                                         @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeNewProductDto> homeBrandList = homeNewProductService.listWx(productName, recommendStatus);
        return CommonResult.success(homeBrandList);
    }
}
