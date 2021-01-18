package com.macro.mall.mallwechat.service.impl;
import com.macro.mall.mallwechat.service.SmsHomeAdvertiseService;
import com.macro.mall.mapper.CmsHelpCategoryMapper;
import com.macro.mall.mapper.SmsHomeAdvertiseMapper;
import com.macro.mall.model.SmsHomeAdvertise;
import com.macro.mall.model.SmsHomeAdvertiseExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 首页广告管理Service实现类
 * Created by macro on 2018/11/7.
 */
@Service
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;

    @Override
    public List<SmsHomeAdvertise> listAll() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        return advertiseMapper.selectByExample(example);
    }
}
