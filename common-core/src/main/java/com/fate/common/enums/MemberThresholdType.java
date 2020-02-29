package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum MemberThresholdType {
    SCORE_VALUE(0, "积分值"),
    CUMULATIVE_RECHARGE_AMOUNT(1, "累计充值金额"),
    CUMULATIVE_CONSUME_AMOUNT(2, "累计消费金额"),
    CUMULATIVE_CONSUME_NUMBER(3, "累计消费次数"),
    CHARGE_AMOUNT(4, "单次充值金额"),
    CONSUME_AMOUNT(5,"单次消费金额");

    @EnumValue
    private int code;
    private String desc;

    MemberThresholdType(int code, String desc) {
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
    public static Optional<MemberThresholdType> getEnum(Integer code){
        if (code!=null){
            for (MemberThresholdType type: MemberThresholdType.values()){
                if (code.equals(type.getCode())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 根据描述返回枚举类型
     * @param desc
     * @return
     */
    public static Optional<MemberThresholdType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (MemberThresholdType type: MemberThresholdType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
