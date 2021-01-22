package com.macro.mall.service;

import com.macro.mall.model.UmsMember;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    UmsMember register(UmsMember umsMember);

    UmsMember getByOpenId(String openId);

    void update(UmsMember umsMember);
}
