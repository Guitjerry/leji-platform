package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.constant.AuthConstant;
import com.macro.mall.common.util.TokenUtil;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.mapper.UmsMemberMapper;
import com.macro.mall.model.UmsMember;
import com.macro.mall.model.UmsMemberExample;
import com.macro.mall.model.UmsMemberExample.Criteria;
import com.macro.mall.query.UmsMemberQuery;
import com.macro.mall.service.UmsMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 会员管理Service实现类
 * Created by macro on 2018/8/3.
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Autowired
    private UmsMemberMapper memberMapper;
    @Autowired
    private HttpServletRequest request;
    @Override
    public UmsMember register(UmsMember umsMember) {
        umsMember.setCreateTime(new Date());
        //未审核用户
        umsMember.setStatus(CommonConstant.FLAG_NO);
        if(ObjectUtil.isNotNull(umsMember.getUsername())) {
            //查询是否有相同用户名的用户
            UmsMemberExample example = new UmsMemberExample();
            example.createCriteria().andUsernameEqualTo(umsMember.getUsername());
            List<UmsMember> members = memberMapper.selectByExample(example);
            if (members.size() > 0) {
                return null;
            }
        }

        //将密码进行加密操作
        if(ObjectUtil.isNotNull(umsMember.getPassword())) {
            String encodePassword = BCrypt.hashpw(umsMember.getPassword());
            umsMember.setPassword(encodePassword);
        }

        memberMapper.insert(umsMember);
        return umsMember;
    }

    @Override
    public UmsMember getByOpenId(String openId) {
        UmsMemberExample memberExample = new UmsMemberExample();
        memberExample.or().andOpenIdEqualTo(openId);
        List<UmsMember> members = memberMapper.selectByExample(memberExample);
        return CollectionUtil.isNotEmpty(members)? members.get(0): null;
    }

    @Override
    public void update(UmsMember umsMember) {
        memberMapper.updateByPrimaryKeySelective(umsMember);
    }

  @Override
  public List<UmsMember> list(Integer pageNum, Integer pageSize, UmsMemberQuery umsMemberQuery) {
    PageHelper.startPage(pageNum, pageSize);
    UmsMemberExample umsMemberExample= new UmsMemberExample();
    Criteria criteria = umsMemberExample.or();
    if(ObjectUtil.isNotNull(umsMemberQuery.getUsername())) {
      criteria.andUsernameLike(umsMemberQuery.getUsername());
    }
    if(ObjectUtil.isNotNull(umsMemberQuery.getPhone())) {
        criteria.andPhoneEqualTo(umsMemberQuery.getPhone());
    }
    return  memberMapper.selectByExample(umsMemberExample);
  }

  @Override
  public void configAdmin(Long memberId) {
    UmsMember umsMember = memberMapper.selectByPrimaryKey(memberId);
    umsMember.setPosition(CommonConstant.FLAG_YES);
    memberMapper.updateByPrimaryKey(umsMember);
  }

    @Override
    public UmsMember queryById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public UmsMember getCurrMember() {
        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        Assert.isTrue(StrUtil.isNotEmpty(token), "获取token失败");
        String openId = TokenUtil.validateToken(token.replace(AuthConstant.JWT_TOKEN_PREFIX, ""));
        Assert.isTrue(StrUtil.isNotEmpty(openId), "获取用户信息失败");
        return getByOpenId(openId);
    }

    @Override
    public UmsMember getById(Long memberId) {
        return memberMapper.selectByPrimaryKey(memberId);
    }

}
