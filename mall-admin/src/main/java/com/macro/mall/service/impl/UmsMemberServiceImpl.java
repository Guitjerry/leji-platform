package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.github.pagehelper.PageHelper;
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
    @Override
    public UmsMember register(UmsMember umsMember) {
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(CommonConstant.FLAG_YES);
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
    umsMember.setPostion(CommonConstant.FLAG_YES);
    memberMapper.updateByPrimaryKey(umsMember);
  }

}
