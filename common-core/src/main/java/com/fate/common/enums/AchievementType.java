package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description: 绩效类型
 * @author: chenyixin
 * @create: 2019-07-23 20:37
 **/

public enum  AchievementType {
    COMPREHENSIVE(0, "综合"),
    ORDER(1, "订单量"),
    AMOUNT(2, "累计额"),
    EVALUATE(3, "评价指标");

    @EnumValue
    private int code;
    private String desc;

    AchievementType(int code, String desc) {
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
    public static Optional<AchievementType> getEnum(Integer code){
        if (code!=null){
            for (AchievementType type:AchievementType.values()){
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
    public static Optional<AchievementType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (AchievementType type:AchievementType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
