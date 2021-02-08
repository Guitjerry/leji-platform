package com.macro.mall.query;

import io.swagger.annotations.ApiModelProperty;

public class UmsMemberQuery {
  @ApiModelProperty(value = "用户名")
  private String username;
  @ApiModelProperty(value = "手机号码")
  private String phone;
  @ApiModelProperty(value = "性别：0->未知；1->男；2->女")
  private Integer gender;
  @ApiModelProperty(value = "会员等级")
  private Integer memberLevel;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public Integer getMemberLevel() {
    return memberLevel;
  }

  public void setMemberLevel(Integer memberLevel) {
    this.memberLevel = memberLevel;
  }
}
