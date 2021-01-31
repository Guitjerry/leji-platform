package com.macro.mall.enums;

public enum OrderPaySourceEnum {
    /**
     * PC订单
     */
    PC(0, "PC订单"),

    /**
     * 小程序订单
     */
    WECHAT(1, "小程序订单");


    private int key;

    private String value;

    OrderPaySourceEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static OrderPaySourceEnum getOrderPaySourceEnum(int key) {
        for (OrderPaySourceEnum paySourceEnum : OrderPaySourceEnum.values()) {
            if (paySourceEnum.getKey() == key) {
                return paySourceEnum;
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
