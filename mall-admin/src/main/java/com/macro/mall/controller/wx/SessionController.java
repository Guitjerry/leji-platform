package com.macro.mall.controller.wx;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.constant.AuthConstant;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.common.service.RedisService;
import com.macro.mall.dto.SessionDto;
import com.macro.mall.dto.UmsAdminParam;
import com.macro.mall.model.UmsAdmin;
import com.macro.mall.model.UmsMember;
import com.macro.mall.query.WechatLoginQuery;
import com.macro.mall.service.UmsAdminService;
import com.macro.mall.service.UmsMemberService;
import com.macro.mall.service.WechatService;
import com.macro.mall.util.TokenUtil;
import com.macro.mall.vo.LoginInfoVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import com.macro.mall.util.AES;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    @Autowired
    private UmsMemberService userMemberService;
    @Autowired
    private RedisService redisService;

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
    CommonResult<LoginInfoVo> wetchatLogin(@RequestBody WechatLoginQuery wechatLoginQuery) {
        if (ObjectUtil.isNull(wechatLoginQuery.getWechatCode())) {
            Asserts.fail("code不存在");
        }
        //通过openid查询用户信息
        SessionDto sessionDto = wechatService.getOpenIdByCode(wechatLoginQuery.getWechatCode());
        if (ObjectUtil.isNull(sessionDto)) {
            Asserts.fail("校验获取用户失败");
        }
        String openId = sessionDto.getOpenId();
        String token = null;
        System.out.println("获取到的openId为" + openId);
        UmsMember umsMember = userMemberService.getByOpenId(openId);
        if (ObjectUtil.isNull(umsMember)) {
            umsMember = new UmsMember();
            umsMember.setOpenId(openId);
            //注册新用户
            umsMember = userMemberService.register(umsMember);
        }
        try {
            //token
          token = TokenUtil.buildJWT(umsMember.getOpenId(), umsMember.getId(), umsMember.getPhone());
        } catch (Exception e) {
            Assert.isTrue(false, "token获取失败");
        }
        LoginInfoVo loginInfoVo = LoginInfoVo.builder().status(umsMember.getStatus())
                .phone(umsMember.getPhone()).openId(openId).token(token).id(umsMember.getId()).build();
        return CommonResult.success(loginInfoVo);
    }


    @RequestMapping(value = "/wetchatGetPhone", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<UmsMember> wetchatGetPhone(String encryptedData, String iv, String code) {
        SessionDto sessionDto = wechatService.getOpenIdByCode(code);
        String paramObject = AES.wxDecrypt(encryptedData, sessionDto.getSessionKey(), iv);
        JSONObject param = JSONUtil.parseObj(paramObject);
        UmsMember umsMember = null;
        if (ObjectUtil.isNotNull(param)) {
            String phone = param.getStr("phoneNumber");
            //更新用户手机信息
            umsMember = userMemberService.getByOpenId(sessionDto.getOpenId());
            umsMember.setPhone(phone);
            userMemberService.update(umsMember);
        }
        return CommonResult.success(umsMember);
    }
}
