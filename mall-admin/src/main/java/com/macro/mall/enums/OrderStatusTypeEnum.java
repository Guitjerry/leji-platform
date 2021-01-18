package com.macro.mall.enums;

/**
 * 0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单"
 */
public enum OrderStatusTypeEnum {
    /**
     * 待付款
     */
    wait(0, "待付款"),

    /**
     * 待发货
     */
    waitSend(1, "待发货"),

    /**
     * 已发货
     */
    hasSend(2, "已发货"),

    /**
     * 已完成
     */
    hasComplete(3, "已完成"),

    /**
     * 已关闭
     */
    hasClosed(4, "已关闭"),

    /**
     * 无效订单
     */
    unUseful(5, "无效订单");

    private int key;

    private String value;

    OrderStatusTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static OrderStatusTypeEnum getOrgTypeEnum(int key) {
        for (OrderStatusTypeEnum orderStatusTypeEnum : OrderStatusTypeEnum.values()) {
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
