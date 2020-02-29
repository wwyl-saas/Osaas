package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum ChargeType {
    OFFLINE_CHARGE(0,"线下充值"),
    ONLINE_WX_CHARGE(1,"微信充值");

    @EnumValue
    private final int code;
    private String desc;

    ChargeType(int code, String desc) {
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
    public static Optional<ChargeType> getEnum(Integer code){
        for (ChargeType type: ChargeType.values()){
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
    public static Optional<ChargeType> getEnum(String desc){
        for (ChargeType type: ChargeType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
