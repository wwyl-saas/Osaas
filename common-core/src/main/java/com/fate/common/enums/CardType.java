package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum CardType {
    DEDUCTION_CARD(0,"抵扣卡"),
    DISCOUNT_CARD(1,"折扣卡"),
    PACKAGE_CARD(2,"套餐卡");

    @EnumValue
    private final int code;
    private String desc;

    CardType(int code,String desc) {
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
    public static Optional<CardType> getEnum(Integer code){
        for (CardType type:CardType.values()){
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
    public static Optional<CardType> getEnum(String desc){
        for (CardType type:CardType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
