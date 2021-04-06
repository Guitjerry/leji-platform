package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.common.exception.ApiException;
import com.macro.mall.common.util.BeanCopyUtil;
import com.macro.mall.constant.CommonConstant;
import com.macro.mall.dao.SmsCouponDao;
import com.macro.mall.dao.SmsCouponProductCategoryRelationDao;
import com.macro.mall.dao.SmsCouponProductRelationDao;
import com.macro.mall.dto.SmsCouponDto;
import com.macro.mall.dto.SmsCouponParam;
import com.macro.mall.enums.CouponStatusEnum;
import com.macro.mall.mapper.SmsCouponHistoryMapper;
import com.macro.mall.mapper.SmsCouponMapper;
import com.macro.mall.mapper.SmsCouponProductCategoryRelationMapper;
import com.macro.mall.mapper.SmsCouponProductRelationMapper;
import com.macro.mall.model.*;
import com.macro.mall.resp.SmsCouponResp;
import com.macro.mall.service.SmsCouponService;
import com.macro.mall.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券管理Service实现类
 * Created by macro on 2018/8/28.
 */
@Service
public class SmsCouponServiceImpl implements SmsCouponService {
    @Autowired
    private SmsCouponMapper couponMapper;
    @Autowired
    private SmsCouponProductRelationMapper productRelationMapper;
    @Autowired
    private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;
    @Autowired
    private SmsCouponProductRelationDao productRelationDao;
    @Autowired
    private SmsCouponProductCategoryRelationDao productCategoryRelationDao;
    @Autowired
    private SmsCouponDao couponDao;
    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;
    @Autowired
    private SmsCouponMapper smsCouponMapper;
    @Override
    public int create(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);
        //插入优惠券表
        int count = couponMapper.insert(couponParam);
        //插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            productRelationDao.insertList(couponParam.getProductRelationList());
        }
        //插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            productCategoryRelationDao.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    @Override
    public int delete(Long id) {
        //删除优惠券
        int count = couponMapper.deleteByPrimaryKey(id);
        //删除商品关联
        deleteProductRelation(id);
        //删除商品分类关联
        deleteProductCategoryRelation(id);
        return count;
    }

    private void deleteProductCategoryRelation(Long id) {
        SmsCouponProductCategoryRelationExample productCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
        productCategoryRelationExample.createCriteria().andCouponIdEqualTo(id);
        productCategoryRelationMapper.deleteByExample(productCategoryRelationExample);
    }

    private void deleteProductRelation(Long id) {
        SmsCouponProductRelationExample productRelationExample = new SmsCouponProductRelationExample();
        productRelationExample.createCriteria().andCouponIdEqualTo(id);
        productRelationMapper.deleteByExample(productRelationExample);
    }

    @Override
    public int update(Long id, SmsCouponParam couponParam) {
        couponParam.setId(id);
        int count =couponMapper.updateByPrimaryKey(couponParam);
        //删除后插入优惠券和商品关系表
        if(couponParam.getUseType().equals(2)){
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(id);
            productRelationDao.insertList(couponParam.getProductRelationList());
        }
        //删除后插入优惠券和商品分类关系表
        if(couponParam.getUseType().equals(1)){
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(id);
            productCategoryRelationDao.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    @Override
    public List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        SmsCouponExample example = new SmsCouponExample();
        SmsCouponExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }
        if(type!=null){
            criteria.andTypeEqualTo(type);
        }
        PageHelper.startPage(pageNum,pageSize);
        return couponMapper.selectByExample(example);
    }

    @Override
    public SmsCouponParam getItem(Long id) {
        return couponDao.getItem(id);
    }

    /**
     * 微信查询优惠券
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    @Override
    public SmsCouponResp listWxCoupon(Integer pageNum, Integer pageSize, String token) {
        SmsCouponResp smsCouponResp = new SmsCouponResp();
       try {
           Long id = TokenUtil.getIdByToken(token);
           //查询所有的可领取优惠券
           List<SmsCouponDto> smsCouponDtos =  productRelationDao.listAvailableCoupons(id);
           smsCouponDtos = smsCouponDtos.stream()
                   .filter(smsCouponDto -> smsCouponDto.getPublishCount() > smsCouponDto.getReceiveCount()
                           && smsCouponDto.getEndTime().getTime() > new Date().getTime()).collect(Collectors.toList());
           if(CollectionUtil.isNotEmpty(smsCouponDtos)) {
               smsCouponResp.setAvailableCoupons(smsCouponDtos);
           }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        }
        return smsCouponResp;
    }

    /**
     * 领取优惠券
     * 1.校验资格
     * 2.领取记录到history
     * 3.更新优惠券数量
     * @param memberId
     * @return
     */
    @Override
    public SmsCoupon receiveCoupon(Long memberId, Long couponId) throws Exception{
        //校验领取资格
        vaildreceiveCoupon(memberId, couponId);
        //记录领取信息
        SmsCoupon smsCoupon =  couponMapper.selectByPrimaryKey(couponId);
        SmsCouponHistory smsCouponHistory = new SmsCouponHistory();
        smsCouponHistory.setCouponId(couponId);
        smsCouponHistory.setCouponCode(smsCoupon.getCode());
        smsCouponHistory.setCreateTime(DateUtil.parseDate(DateUtil.now()));
        smsCouponHistory.setMemberId(memberId);
        smsCouponHistory.setUseStatus(CommonConstant.FLAG_NO);
        smsCouponHistory.setGetType(CommonConstant.FLAG_YES);
        smsCouponHistoryMapper.insertSelective(smsCouponHistory);
        return smsCoupon;
    }

    /**
     * 我的优惠券
     * @param memberId
     * @return
     */
    @Override
    public SmsCouponResp myCoupon(Long memberId) {
        SmsCouponResp smsCouponResp = null;
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.or().andMemberIdEqualTo(memberId);
        List<SmsCouponHistory>  smsCouponHistories =  smsCouponHistoryMapper.selectByExample(historyExample);

        if(CollectionUtil.isNotEmpty(smsCouponHistories)) {
            smsCouponResp = new SmsCouponResp();
            //未使用
            List<SmsCouponHistory> unUseCoupon = smsCouponHistories.stream()
                    .filter(smsCouponHistory -> smsCouponHistory.getUseStatus().equals(CouponStatusEnum.UNUSE.getKey())).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(unUseCoupon)) {
                List<Long> unUseCouponIds = unUseCoupon.stream().map(SmsCouponHistory::getCouponId).collect(Collectors.toList());
                SmsCouponExample couponExample = new SmsCouponExample();
                couponExample.or().andIdIn(unUseCouponIds);
                List<SmsCoupon> smsCoupons = smsCouponMapper.selectByExample(couponExample);
                if(CollectionUtil.isNotEmpty(smsCoupons)) {
                   List<SmsCouponDto> smsCouponDtos =  smsCoupons.stream().map(smsCoupon -> {
                        SmsCouponDto smsCouponDto = BeanCopyUtil.transform(smsCoupon, SmsCouponDto.class);
                        //3天内为即将到期
                        if(smsCouponDto.getEndTime().getTime() - new Date().getTime()<= 1000*60*60*24*3) {
                            smsCouponDto.setNearExpire(CommonConstant.FLAG_YES);
                        }
                        return smsCouponDto;
                    }).collect(Collectors.toList());
                    smsCouponResp.setUnUseCoupons(smsCouponDtos);
                }
            }

            //已使用
            List<SmsCouponHistory> usedCoupon = smsCouponHistories.stream()
                    .filter(smsCouponHistory -> smsCouponHistory.getUseStatus().equals(CouponStatusEnum.USED.getKey())).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(usedCoupon)) {
                List<Long> unUseCouponIds = usedCoupon.stream().map(SmsCouponHistory::getCouponId).collect(Collectors.toList());
                SmsCouponExample couponExample = new SmsCouponExample();
                couponExample.or().andIdIn(unUseCouponIds);
                List<SmsCoupon> smsCoupons = smsCouponMapper.selectByExample(couponExample);
                if(CollectionUtil.isNotEmpty(smsCoupons)) {
                    List<SmsCouponDto> smsCouponDtos = BeanCopyUtil.transform(smsCoupons, SmsCouponDto.class);
                    smsCouponResp.setUsedCoupons(smsCouponDtos);
                }
            }

            //已过期
            List<Long> couponIds = smsCouponHistories.stream()
                    .map(SmsCouponHistory::getCouponId).collect(Collectors.toList());
            SmsCouponExample couponExample = new SmsCouponExample();
            couponExample.or().andIdIn(couponIds);
            List<SmsCoupon> smsCoupons = smsCouponMapper.selectByExample(couponExample);
            smsCoupons = smsCoupons.stream().filter(smsCoupon -> smsCoupon.getEndTime().after(new Date())).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(smsCoupons)) {
                List<SmsCouponDto> smsCouponDtos = BeanCopyUtil.transform(smsCoupons, SmsCouponDto.class);
                smsCouponResp.setExpireCoupons(smsCouponDtos);
            }

        }
        return smsCouponResp;
    }

    /**
     * 校验领取资格
     * @param memberId
     * @param couponId
     */
    private void vaildreceiveCoupon(Long memberId, Long couponId) throws Exception{
       SmsCoupon smsCoupon =  couponMapper.selectByPrimaryKey(couponId);
       //校验可用数量
       Integer canUseCount = smsCoupon.getPublishCount() -  smsCoupon.getReceiveCount();
       //校验到期
       if(smsCoupon.getEndTime().getTime() < new Date().getTime()) {
           throw new ApiException("优惠券已过期，无法领取");
        }

       if(canUseCount<=0) {
           throw new ApiException("当前优惠券已经被抢完，请下次再试");
       }
       //查询领取
        SmsCouponHistoryExample historyExample = new SmsCouponHistoryExample();
        historyExample.or().andCouponIdEqualTo(couponId).andMemberIdEqualTo(memberId);
        Long count = smsCouponHistoryMapper.countByExample(historyExample);
        Integer perLimit = smsCoupon.getPerLimit();
        if(perLimit < count) {
            throw new ApiException("您已经领取该优惠券了，不无法重复领取");
        }
    }
}
