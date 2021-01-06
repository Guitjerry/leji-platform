package com.macro.mall.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Date 2020/12/6 13:39
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@Data
public class WechatLoginQuery {
    @ApiModelProperty("注册时填写的手机号")
    private String mobile;
    private String wechatCode;
}
