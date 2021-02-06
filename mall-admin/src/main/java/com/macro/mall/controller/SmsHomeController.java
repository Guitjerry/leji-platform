package com.macro.mall.controller;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.HomeResultDto;
import com.macro.mall.model.SmsHomeBrand;
import com.macro.mall.service.HomeService;
import com.macro.mall.service.SmsHomeBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页品牌管理Controller
 * Created by macro on 2018/11/6.
 */
@Controller
@Api(tags = "SmsHomeController", description = "首页")
@RequestMapping("/home")
public class SmsHomeController {
    @Autowired
    private HomeService homeBrandService;

    @ApiOperation("首页信息显示")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult index() {
        HomeResultDto homeResultDto = homeBrandService.index();
        return CommonResult.success(homeResultDto);
    }

}
