package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum FeedBackType {

    APPLICATION_PROBLEM(0, "程序问题"),
    GOOD_PROBLEM(1, "商品问题"),
    FINANCE_PROBLEM(2, "财务问题"),
    SERVICE_PROBLEM(3, "服务问题"),
    COUPON_PROBLEM(4, "优惠券问题"),
    OTHERS_PROBLEM(5, "其它");


    @EnumValue
    private final int code;
    private String desc;

    FeedBackType(int code, String desc) {
        this.code = code;
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
     *
     * @param code
     * @return
     */
    public static Optional<FeedBackType> getEnum(Integer code) {
        for (FeedBackType type : FeedBackType.values()) {
            if (code.equals(type.getCode())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据描述返回枚举类型
     *
     * @param desc
     * @return
     */
    public static Optional<FeedBackType> getEnum(String desc) {
        for (FeedBackType type : FeedBackType.values()) {
            if (desc.equals(type.getDesc())) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
