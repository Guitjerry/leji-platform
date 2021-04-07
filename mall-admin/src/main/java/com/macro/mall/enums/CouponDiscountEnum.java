package com.macro.mall.enums;

/**
 * 优惠类型 1满减 2折扣 3优惠券
 */
public enum CouponDiscountEnum {
    /**
     * 满减
     */
    MAN(1, "满减"),

    /**
     * 折扣
     */
    ZHE(2, "折扣"),

    /**
     * 优惠券
     */
    COUPON(3, "优惠券");

    private int key;

    private String value;

    CouponDiscountEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static CouponDiscountEnum getCouponStatusEnum(int key) {
        for (CouponDiscountEnum payTypeEnum : CouponDiscountEnum.values()) {
            if (payTypeEnum.getKey() == key) {
                return payTypeEnum;
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
