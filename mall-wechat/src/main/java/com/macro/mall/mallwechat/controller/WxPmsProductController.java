package com.macro.mall.mallwechat.controller;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.dto.PmsProductQueryParam;
import com.macro.mall.mallwechat.dto.PmsProductResult;
import com.macro.mall.mallwechat.service.PmsProductService;
import com.macro.mall.model.PmsProduct;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信商品管理Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "PmsProductController", description = "商品管理")
@RequestMapping("/wxApi/product")
public class WxPmsProductController {
    @Autowired
    private PmsProductService productService;

    @ApiOperation("查询商品")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<PmsProduct>> getList(PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = Lists.newArrayList();
        if(ObjectUtil.isNotNull(productQueryParam.getType())) {
            productList  = productService.listByType(productQueryParam.getType(), pageSize, pageNum);
        }else {
            productList = productService.list(productQueryParam, pageSize, pageNum);
        }
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PmsProductResult> getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = productService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }


}
