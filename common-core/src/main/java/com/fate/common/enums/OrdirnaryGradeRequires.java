package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 普通等级要求
 */
public enum OrdirnaryGradeRequires {
    GRADE_REQUIRES(0, "积分要求"),
    CONSUME_REQUIRES(1, "消费次数"),
    RECHARGE_REQUIRES(  2, "充值金额");


    @EnumValue
    private int code;
    private String desc;

    OrdirnaryGradeRequires(int code, String desc) {
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
    public static Optional<OrdirnaryGradeRequires> getEnum(Integer code){
        if (code!=null){
            for (OrdirnaryGradeRequires type: OrdirnaryGradeRequires.values()){
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
    public static Optional<OrdirnaryGradeRequires> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (OrdirnaryGradeRequires type: OrdirnaryGradeRequires.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
