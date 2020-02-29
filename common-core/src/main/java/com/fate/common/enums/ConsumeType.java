package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Optional;

/**
 * @program: parent
 * @description: 用户的流水类型
 * @author: chenyixin
 * @create: 2019-05-27 23:59
 **/
public enum ConsumeType {
    REMAINDER(0,"余额支付"),
    WX_PAY(1,"微信支付");

    @EnumValue
    private final int code;
    private String desc;

    ConsumeType(int code, String desc) {
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
    public static Optional<ConsumeType> getEnum(Integer code){
        for (ConsumeType type: ConsumeType.values()){
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
    public static Optional<ConsumeType> getEnum(String desc){
        for (ConsumeType type: ConsumeType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
