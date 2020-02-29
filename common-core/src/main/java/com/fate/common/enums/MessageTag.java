package com.fate.common.enums;

import java.util.Optional;

/**
 * 集群消费模式的tag
 */
public enum MessageTag {
    /**
     * 预约确认
     */
    APPOINTMENT_CONFIRM("appointment_confirm"),
    /**
     * 商户预约取消
     */
    MERCHANT_APPOINTMENT_CANCEL("merchant_appointment_cancel"),
    /**
     * 预约改约
     */
    APPOINTMENT_CHANGE("appointment_change"),
    /**
     * 预约创建
     */
    APPOINTMENT_CREATE("appointment_create"),
    /**
     * 客户取消预约
     */
    CUSTOMER_APPOINTMENT_CANCEL("customer_appointment_cancel"),
    /**
     * 订单取消
     */
    CUSTOMER_ORDER_CANCEL("customer_order_cancel"),
    /**
     *  订单结算结果
     */
    ORDER_SETTLE("order_settle"),
    /**
     *  充值成功
     */
    RECHARGE_SUCCESS("recharge_success"),
    /**
     * 反馈创建
     */
    FEEDBACK_CREATE("feedback_create"),
    /**
     * 充值成功
     */
    CHARGE_SUCCESS("charge_success"),
    /**
     * 消费成功
     */
    CONSUME_SUCCESS("consume_success");

    private String tag;

    MessageTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    /**
     * 根据描述返回枚举类型
     * @param tag
     * @return
     */
    public static Optional<MessageTag> getEnum(String tag){
        for (MessageTag type:MessageTag.values()){
            if (tag.equals(type.getTag())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
