package com.macro.mall.mallwechat.controller;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.dto.SmsHomeRecommendProductDto;
import com.macro.mall.mallwechat.service.SmsHomeRecommendProductService;
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
                                                               @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeRecommendProductDto> homeBrandList = recommendProductService.listWx(productName, recommendStatus);
        return CommonResult.success(homeBrandList);
    }
}
