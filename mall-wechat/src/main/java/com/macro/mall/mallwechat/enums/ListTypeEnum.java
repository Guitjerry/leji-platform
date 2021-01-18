package com.macro.mall.mallwechat.enums;

public enum ListTypeEnum {
    /**
     * 新品推荐
     */
    NEW(1, "新品推荐"),

    /**
     * 特价推荐
     */
    TEJIA(2, "特价推荐");

    private int key;

    private String value;

    ListTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static ListTypeEnum getOrgTypeEnum(int key) {
        for (ListTypeEnum payTypeEnum : ListTypeEnum.values()) {
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
