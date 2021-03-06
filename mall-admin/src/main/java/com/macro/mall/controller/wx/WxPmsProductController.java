package com.macro.mall.controller.wx;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.util.BeanCopyUtil;
import com.macro.mall.dto.*;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.model.PmsProductCategory;
import com.macro.mall.service.PmsProductService;
import com.macro.mall.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信商品管理Controller Created by macro on 2018/4/26.
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
  public CommonResult<CommonPage<PmsProductParam>> getList(PmsProductQueryParam productQueryParam,
    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
    List<PmsProductParam> productList = Lists.newArrayList();
    if (ObjectUtil.isNotNull(productQueryParam.getType())) {
      productList = productService.listByType(productQueryParam, pageSize, pageNum);
    }else {
      List<PmsProduct> pmsProducts = productService.listBackGround(productQueryParam, pageSize, pageNum);
      productList = BeanCopyUtil.transform(pmsProducts, PmsProductParam.class);
    }
    return CommonResult.success(CommonPage.restPage(productList));
  }

  @ApiOperation("查询分类商品")
  @RequestMapping(value = "/listByCategoryId", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<CommonPage<PmsProductDto>> listByCategoryId(PmsProductQueryParam productQueryParam,
    @RequestParam(value = "pageSize", defaultValue = "100") Integer pageSize,
    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
    List<PmsProductDto> productList = Lists.newArrayList();
    productList = productService.list(productQueryParam, pageSize, pageNum);
    return CommonResult.success(CommonPage.restPage(productList));
  }

  @ApiOperation("根据商品id获取商品编辑信息")
  @RequestMapping(value = "/updateInfo/{id}", method = RequestMethod.GET)
  @ResponseBody
  public CommonResult<PmsProductResult> getUpdateInfo(@PathVariable Long id) {
    PmsProductResult productResult = productService.getUpdateInfo(id);
    return CommonResult.success(productResult);
  }

  @ApiOperation("计算优惠信息")
  @RequestMapping(value = "/queryDiscount", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult<AllCartDiscountDto> queryDiscount(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody OmsOrderPayParam omsOrderPayParam) throws Exception {
    Long id = TokenUtil.getIdByAuthorization(authorization);
    AllCartDiscountDto allCartDiscountDto = productService.queryDiscount(omsOrderPayParam.getCarts(), id);
    return CommonResult.success(allCartDiscountDto);
  }

  @ApiOperation("创建商品")
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public CommonResult create(@RequestBody PmsProductParam productParam) {
    int count = productService.create(productParam);
    if (count > 0) {
      return CommonResult.success(count);
    } else {
      return CommonResult.failed();
    }
  }

}
