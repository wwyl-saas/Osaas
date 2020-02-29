package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Optional;

/**
 * @program: parent
 * @description: 订单状态
 * @author: chenyixin
 * @create: 2019-05-07 19:15
 **/
public enum  OrderStatus {
    WAITING_PAY(0,"待支付"),
    CANCELED(1,"已取消"),
    PAYED(2,"已支付"),
    COMPLETED(3,"已完成"),
    PAYED_ERROR(4,"支付失败");


    @EnumValue
    private final Integer code;
    private String desc;

    OrderStatus(int code,String desc) {
        this.code=code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }
    @JsonValue
    public String getDesc() {
        return desc;
    }


    /**
     * 根据序号返回枚举类型
     * @param code
     * @return
     */
    public static Optional<OrderStatus> getEnum(Integer code){
        for (OrderStatus type:OrderStatus.values()){
            if (type.getCode().equals(code)){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据描述返回枚举类型
     * @param desc
     * @return
     */
    public static Optional<OrderStatus> getEnum(String desc){
        for (OrderStatus type:OrderStatus.values()){
            if (type.getDesc().equals(desc)){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
