package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Optional;

public enum CouponStatus{
    NOT_USED(0,"未使用"),
    LOCKED(1,"已锁定"),
    USED(2,"已使用"),
    EXPIRED(3,"已过期");

    @EnumValue
    private final int code;
    private String desc;

    CouponStatus(int code,String desc) {
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
    public static Optional<CouponStatus> getEnum(Integer code){
        for (CouponStatus type:CouponStatus.values()){
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
    public static Optional<CouponStatus> getEnum(String desc){
        for (CouponStatus type:CouponStatus.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
