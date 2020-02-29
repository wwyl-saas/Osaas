package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Optional;

/**
 * 会员枚举
 */
public enum MemberLevel{
    COMMON(0,"普通会员"),
    SILVER(1,"白银会员"),
    GOLD(2,"黄金会员"),
    PLATINUM(3,"铂金会员"),
    DIAMOND(4,"黑钻会员");


    @EnumValue
    private final Integer code;
    private String desc;

    MemberLevel(Integer code,String desc) {
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
    public static Optional<MemberLevel> getEnum(Integer code){
        for (MemberLevel type:MemberLevel.values()){
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
    public static Optional<MemberLevel> getEnum(String desc){
        for (MemberLevel type:MemberLevel.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
