package com.macro.mall.mallwechat.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.common.domain.UserDto;
import com.macro.mall.model.UmsMember;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    UmsMember register(UmsMember umsMember);

    UmsMember getByOpenId(String openId);

    void update(UmsMember umsMember);
}
