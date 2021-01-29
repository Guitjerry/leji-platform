package com.macro.mall.common.enums;

/**
 * 促销类型：0->没有促销使用原价;1->使用促销价；2->使用会员价；3->使用阶梯价格；4->使用满减价格；5->限时购
 */
public enum CouponTypeEnum {
    /**
     * 全场通用
     */
    COMMON(0, "全场通用"),

    /**
     * 指定分类
     */
    CATEGORY(1, "指定分类"),
    /**
     * 指定商品
     */
    GOOD(2, "指定商品");

    private int key;

    private String value;

    CouponTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static CouponTypeEnum getCouponTypeEnum(int key) {
        for (CouponTypeEnum couponTypeEnum : CouponTypeEnum.values()) {
            if (couponTypeEnum.getKey() == key) {
                return couponTypeEnum;
            }
        }
        return null;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
