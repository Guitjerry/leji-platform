package com.macro.mall.controller.wx;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.dto.UmsAdminParam;
import com.macro.mall.model.UmsAdmin;
import com.macro.mall.model.UmsAdminExample;
import com.macro.mall.query.WechatLoginQuery;
import com.macro.mall.service.UmsAdminService;
import com.macro.mall.service.WechatService;
import com.macro.mall.util.TokenUtil;
import com.macro.mall.vo.LoginInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sun.xml.internal.ws.api.message.Packet.Status.Response;

/**
 *
 */
@Controller
@Api(tags = "sessionController", description = "登录wechat相关")
@RequestMapping("/wxApi/session")
public class SessionController {
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UmsAdminService adminService;

    /**
     * 注册与查询小程序用户信息、
     * 1.根据code查询openId
     * 2.根据openId查询用户的手机号码以及用户信息
     * 3.审核过的用户可以查看相关产品价格
     *
     * @param wechatLoginQuery
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/wetchatLogin", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<LoginInfoVo> wetchatLogin(@RequestBody WechatLoginQuery wechatLoginQuery)  {
        if(ObjectUtil.isNull(wechatLoginQuery.getWechatCode())) {
            Asserts.fail("code不存在");
        }
        //通过openid查询用户信息
        String openId = wechatService.getOpenIdByCode(wechatLoginQuery.getWechatCode());
        String token = null;
        System.out.println("获取到的openId为" + openId);
        UmsAdmin umsAdmin = adminService.getAdminByOpenId(openId);
        if (ObjectUtil.isNull(umsAdmin)) {
            UmsAdminParam adminParam = new UmsAdminParam();
            adminParam.setOpenId(openId);
            BeanUtil.copyProperties(umsAdmin, adminParam);
            //注册新用户
            umsAdmin = adminService.register(adminParam);
        }
        try {
            //token
            token =  TokenUtil.buildJWT(umsAdmin.getOpenId(), umsAdmin.getId(), umsAdmin.getMobile());
        }catch (Exception e) {
            Assert.isTrue(false, "token获取失败");
        }
        LoginInfoVo loginInfoVo = LoginInfoVo.builder().status(umsAdmin.getStatus())
                .mobile(umsAdmin.getMobile()).openId(openId).token(token).uid(umsAdmin.getId()).build();
        return CommonResult.success(loginInfoVo);
    }
}
