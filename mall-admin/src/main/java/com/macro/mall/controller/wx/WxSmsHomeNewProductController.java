package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.SmsHomeNewProductDto;
import com.macro.mall.model.SmsHomeNewProduct;
import com.macro.mall.service.SmsHomeNewProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                                                         @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeNewProductDto> homeBrandList = homeNewProductService.list(productName, recommendStatus, Integer.MAX_VALUE, pageNum);
        return CommonResult.success(homeBrandList);
    }
}
