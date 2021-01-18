package com.macro.mall.mallwechat.service;

import com.macro.mall.model.SmsHomeAdvertise;

import java.util.List;

/**
 * 首页广告管理Service
 * Created by macro on 2018/11/7.
 */
public interface SmsHomeAdvertiseService {
    /**
     * 广告查询 （小程序用到）
     * @return
     */
    List<SmsHomeAdvertise> listAll();
}
