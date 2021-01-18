package com.macro.mall.enums;

public enum PayTypeEnum {
    /**
     * 未支付
     */
    NOPAY(0, "未支付"),

    /**
     * 支付宝
     */
    ZHIFUBAO(1, "1支付宝"),

    /**
     * 微信
     */
    WEIXIN(2, "微信");

    private int key;

    private String value;

    PayTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static PayTypeEnum getOrgTypeEnum(int key) {
        for (PayTypeEnum payTypeEnum : PayTypeEnum.values()) {
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
