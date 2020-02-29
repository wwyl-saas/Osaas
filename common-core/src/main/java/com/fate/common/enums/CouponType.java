package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * 卡券类型
 */
public enum CouponType {
    CASH_COUPON(0,"代金券"),
    DISCOUNT_COUPON(1,"打折券"),
    CHARGE_COUPON(2,"充返券");

    @EnumValue
    private final int code;
    private String desc;

    CouponType(int code,String desc) {
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
    public static Optional<CouponType> getEnum(Integer code){
        for (CouponType type:CouponType.values()){
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
    public static Optional<CouponType> getEnum(String desc){
        for (CouponType type:CouponType.values()){
            if (desc.equals(type.getDesc())){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
