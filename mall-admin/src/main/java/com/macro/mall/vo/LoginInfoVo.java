package com.macro.mall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoVo {
    private Integer status;
    private String token;
    private Long id;
    private String phone;
    private String openId;
}
