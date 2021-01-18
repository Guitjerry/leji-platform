package com.macro.mall.mallwechat.controller;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.exception.Asserts;
import com.macro.mall.mallwechat.dto.SessionDto;
import com.macro.mall.mallwechat.query.WechatLoginQuery;
import com.macro.mall.mallwechat.service.UmsMemberService;
import com.macro.mall.mallwechat.service.WechatService;
import com.macro.mall.mallwechat.util.AESUtil;
import com.macro.mall.mallwechat.util.TokenUtil;
import com.macro.mall.mallwechat.vo.LoginInfoVo;
import com.macro.mall.model.UmsMember;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private UmsMemberService memberService;
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
        SessionDto sessionDto = wechatService.getOpenIdByCode(wechatLoginQuery.getWechatCode());
        if(ObjectUtil.isNull(sessionDto)) {
            Asserts.fail("校验获取用户失败");
        }
        String openId = sessionDto.getOpenId();
        String token = null;
        System.out.println("获取到的openId为" + openId);
        UmsMember umsMember = memberService.getByOpenId(openId);
        if (ObjectUtil.isNull(umsMember)) {
            umsMember = new UmsMember();
            umsMember.setOpenId(openId);
            //注册新用户
            umsMember = memberService.register(umsMember);
        }
        try {
            //token
            token =  TokenUtil.buildJWT(umsMember.getOpenId(), umsMember.getId(), umsMember.getPhone());
        }catch (Exception e) {
            Assert.isTrue(false, "token获取失败");
        }
        LoginInfoVo loginInfoVo = LoginInfoVo.builder().status(umsMember.getStatus())
                .mobile(umsMember.getPhone()).openId(openId).token(token).uid(umsMember.getId()).build();
        return CommonResult.success(loginInfoVo);
    }



    @RequestMapping(value = "/wetchatGetPhone", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<String> wetchatGetPhone(String encryptedData, String iv, String code, String openId)  {
        SessionDto sessionDto = wechatService.getOpenIdByCode(code);
        String telPhone = AESUtil.wxDecrypt(encryptedData, sessionDto.getSessionKey(), iv);
        JSONObject telObject = JSONUtil.parseObj(telPhone);
        //更新用户手机信息
        UmsMember umsMember = memberService.getByOpenId(openId);
        umsMember.setPhone(telPhone);
        memberService.update(umsMember);
        telPhone = JSONUtil.parseObj(telPhone).get("phoneNumber").toString();
        return CommonResult.success(telPhone);
    }
}
