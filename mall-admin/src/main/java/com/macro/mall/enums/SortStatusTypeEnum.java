package com.macro.mall.enums;

/**
 * 0->综合；1->销量；2->价格
 */
public enum SortStatusTypeEnum {
    /**
     * 综合
     */
    DEFAULT(0, "综合"),

    /**
     * 销量
     */
    SALECOUNT(1, "销量"),

    /**
     * 价格
     */
    PRICE(2, "价格");

    private int key;

    private String value;

    SortStatusTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static SortStatusTypeEnum getOrgTypeEnum(int key) {
        for (SortStatusTypeEnum orderStatusTypeEnum : SortStatusTypeEnum.values()) {
            if (orderStatusTypeEnum.getKey() == key) {
                return orderStatusTypeEnum;
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
