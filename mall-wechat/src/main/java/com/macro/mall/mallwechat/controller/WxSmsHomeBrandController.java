package com.macro.mall.mallwechat.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.dto.SmsHomeBrandDto;
import com.macro.mall.mallwechat.service.SmsHomeBrandService;
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
 * 首页品牌管理Controller
 * Created by macro on 2018/11/6.
 */
@Controller
@Api(tags = "WxSmsHomeBrandController", description = "首页品牌管理")
@RequestMapping("/wxApi/home/brand")
public class WxSmsHomeBrandController {
    @Autowired
    private SmsHomeBrandService homeBrandService;

    @ApiOperation("查询推荐品牌")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsHomeBrandDto>> list(@RequestParam(value = "brandName", required = false) String brandName,
                                                    @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus) {
        List<SmsHomeBrandDto> homeBrandList = homeBrandService.listWx(brandName, recommendStatus);
        return CommonResult.success(homeBrandList);
    }
}
