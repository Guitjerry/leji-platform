package com.macro.mall.service;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.UmsMember;
import com.macro.mall.query.UmsMemberQuery;
import java.util.List;

/**
 * 会员管理Service
 * Created by macro on 2018/8/3.
 */
public interface UmsMemberService {
    UmsMember register(UmsMember umsMember);

    UmsMember getByOpenId(String openId);

    void update(UmsMember umsMember);

    List<UmsMember> list(Integer pageNum, Integer pageSize, UmsMemberQuery umsMember);

    void configAdmin(Long memberId);

    UmsMember queryById(Long id);

    /**
     * 获取小程序当前登录会员
     * @return
     */
    UmsMember getCurrMember();

    UmsMember getById(Long memberId);
}
