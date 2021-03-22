package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.PmsProductCategoryParam;
import com.macro.mall.dto.PmsProductCategoryWithChildrenItem;
import com.macro.mall.model.PmsBrand;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductCategory;
import com.macro.mall.service.PmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查询分类下所有产品品牌")
    @RequestMapping(value = "/findBrandByCategoryId/{categotyId}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<PmsBrand>> findBrandByCategoryId(@PathVariable Long categotyId) {
        List<PmsBrand> list = productCategoryService.findBrandByCategoryId(categotyId);
        return CommonResult.success(list);
    }
}
