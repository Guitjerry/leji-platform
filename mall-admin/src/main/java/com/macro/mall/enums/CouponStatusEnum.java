package com.macro.mall.enums;

/**
 * 使用状态：0->未使用；1->已使用；2->已过期
 */
public enum CouponStatusEnum {
    /**
     * 未使用
     */
    UNUSE(0, "未使用"),

    /**
     * 已使用
     */
    USED(1, "已使用"),

    /**
     * 已过期
     */
    EXPAIRE(2, "已过期");

    private int key;

    private String value;

    CouponStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static CouponStatusEnum getCouponStatusEnum(int key) {
        for (CouponStatusEnum payTypeEnum : CouponStatusEnum.values()) {
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
