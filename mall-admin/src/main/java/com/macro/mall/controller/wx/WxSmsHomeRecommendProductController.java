package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.SmsHomeRecommendProductDto;
import com.macro.mall.model.SmsHomeRecommendProduct;
import com.macro.mall.service.CmsSubjectService;
import com.macro.mall.service.SmsHomeRecommendProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页人气推荐管理Controller
 * Created by macro on 2018/11/6.
 */
@Controller
@Api(tags = "WxSmsHomeRecommendProductController", description = "首页人气推荐管理")
@RequestMapping("/wxApi/home/recommendProduct")
public class WxSmsHomeRecommendProductController {
    @Autowired
    private SmsHomeRecommendProductService recommendProductService;

    @ApiOperation("人气推荐")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsHomeRecommendProductDto>> list(@RequestParam(value = "productName", required = false) String productName,
                                                                  @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeRecommendProductDto> homeBrandList = recommendProductService.list(productName, recommendStatus, Integer.MAX_VALUE, pageNum);
        return CommonResult.success(homeBrandList);
    }
}
