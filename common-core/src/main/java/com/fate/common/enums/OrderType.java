package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Optional;

/**
 * 订单类型
 * @author yanpengfang
 * @create 2019-03-05 9:28 PM
 *
 */
public enum OrderType{
    OTHER(0,"其他"),
    BUSINESS(1,"电商"),
    COSMETOLOGY(2,"美业");


    @EnumValue
    private final int code;
    private String desc;

    OrderType(int code,String desc) {
        this.code=code;
        this.desc = desc;
    }

    public int getCode() {
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
    public static Optional<OrderType> getEnum(Integer code){
        for (OrderType type:OrderType.values()){
            if (code.equals(type.getCode())){
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
    public static Optional<OrderType> getEnum(String desc){
        for (OrderType type:OrderType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
