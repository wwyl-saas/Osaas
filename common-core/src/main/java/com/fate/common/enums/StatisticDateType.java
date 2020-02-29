package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum StatisticDateType {
    DAY(0, "日"),
    WEEK(1, "周"),
    MONTH(2, "月"),
    QUARTER(3, "季度"),
    YEAR(4, "年度");

    @EnumValue
    private int code;
    private String desc;

    StatisticDateType(int code, String desc) {
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
    public static Optional<StatisticDateType> getEnum(Integer code){
        if (code!=null){
            for (StatisticDateType type: StatisticDateType.values()){
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
    public static Optional<StatisticDateType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (StatisticDateType type: StatisticDateType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
