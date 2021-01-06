package com.macro.mall.controller.wx;

import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.SmsHomeBrandDto;
import com.macro.mall.model.SmsHomeBrand;
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
@Api(tags = "WxSmsHomeBrandController", description = "首页品牌管理")
@RequestMapping("/wxApi/home/brand")
public class WxSmsHomeBrandController {
    @Autowired
    private SmsHomeBrandService homeBrandService;

    @ApiOperation("查询推荐品牌")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<SmsHomeBrandDto>> list(@RequestParam(value = "brandName", required = false) String brandName,
                                                          @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeBrandDto> homeBrandList = homeBrandService.list(brandName, recommendStatus, Integer.MAX_VALUE, pageNum);
        return CommonResult.success(homeBrandList);
    }
}
