package com.macro.mall.mallwechat.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.service.PmsProductCategoryService;
import com.macro.mall.model.PmsProductCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类模块Controller
 * Created by macro on 2018/4/26.
 */
@Controller
@Api(tags = "PmsProductCategoryController", description = "商品分类管理")
@RequestMapping("/wxApi/productCategory")
public class WxPmsProductCategoryController {
    @Autowired
    private PmsProductCategoryService productCategoryService;

    @ApiOperation("分页查询商品分类")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductCategory>> getList() {
        List<PmsProductCategory> productCategoryList = productCategoryService.getListAll();
        return CommonResult.success(productCategoryList);
    }

    @ApiOperation("查询所有一级分类及子分类")
    @RequestMapping(value = "/list/withChildren", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsProductCategory>> listWithChildren() {
        List<PmsProductCategory> list = productCategoryService.listCategoryTree();
        return CommonResult.success(list);
    }
}
