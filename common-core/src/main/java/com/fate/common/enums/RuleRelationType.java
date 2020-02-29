package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum RuleRelationType {
    EQUAL(0, "等于"),
    GREATER(1, "大于"),
    LESS(2, "小于"),
    GREATER_EQUAL(3, "大于等于"),
    LESS_EQUAL(4, "小于等于"),
    BETWEEN(5, "介于"),
    INCLUDE(6, "包含"),
    EXCLUDE(7, "不包含");

    @EnumValue
    private int code;
    private String desc;

    RuleRelationType(int code, String desc) {
        this.code = code;
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
    public static Optional<RuleRelationType> getEnum(Integer code){
        if (code!=null){
            for (RuleRelationType type: RuleRelationType.values()){
                if (code.equals(type.getCode())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }

}
