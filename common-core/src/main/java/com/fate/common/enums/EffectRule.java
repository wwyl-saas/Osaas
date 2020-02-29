package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 卡券触发规则
 */
public enum EffectRule {
    ORDER_AMOUNT(1, "订单总金额"),
    ORDER_GOODS_NUM(2, "订单商品数"),
    GOODS_AGGREGATION(3,"商品范围"),
    CONSUME_DATE(4,"消费日期");

    @EnumValue
    private int code;
    private String desc;

    EffectRule(int code, String desc) {
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
    public static Optional<EffectRule> getEnum(Integer code){
        if (code!=null){
            for (EffectRule type: EffectRule.values()){
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
    public static Optional<EffectRule> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (EffectRule type: EffectRule.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
