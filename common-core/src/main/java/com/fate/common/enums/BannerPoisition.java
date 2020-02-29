package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum BannerPoisition {
    INDEX(0,"首页");

    @EnumValue
    private final int code;
    private String desc;

    BannerPoisition(int code,String desc) {
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
    public static Optional<BannerPoisition> getEnum(Integer code){
        for (BannerPoisition type:BannerPoisition.values()){
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
    public static Optional<BannerPoisition> getEnum(String desc){
        for (BannerPoisition type:BannerPoisition.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
