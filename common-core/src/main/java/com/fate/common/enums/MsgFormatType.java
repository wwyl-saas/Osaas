package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * 消息格式
 */
public enum MsgFormatType {
    JSON(0,"JSON"),
    XML(1,"XML");


    @EnumValue
    private final int code;
    private String desc;

    MsgFormatType(int code,String desc) {
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
    public static Optional<MsgFormatType> getEnum(Integer code){
        for (MsgFormatType type:MsgFormatType.values()){
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
    public static Optional<MsgFormatType> getEnum(String desc){
        for (MsgFormatType type:MsgFormatType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
