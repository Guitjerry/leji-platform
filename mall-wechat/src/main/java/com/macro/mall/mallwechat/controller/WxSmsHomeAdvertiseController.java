package com.macro.mall.mallwechat.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.mallwechat.service.SmsHomeAdvertiseService;
import com.macro.mall.model.SmsHomeAdvertise;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 首页轮播广告管理Controller
 * Created by macro on 2018/11/7.
 */
@Controller
@Api(tags = "SmsWxHomeAdvertiseController", description = "首页轮播广告管理")
@RequestMapping("/wxApi/home/advertise")
public class WxSmsHomeAdvertiseController {
    @Autowired
    private SmsHomeAdvertiseService advertiseService;

    @ApiOperation("广告查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsHomeAdvertise> > list() {
        List<SmsHomeAdvertise> advertiseList = advertiseService.listAll();
        return CommonResult.success(advertiseList);
    }
}
