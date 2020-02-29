package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * 业务类型
 */
public enum BusinessType {
    WX_APPOINTMENT_CREATE(0, "微信创建预约"),
    WX_PAY_CREATE(1, "发起确认订单支付"),
    WX_CHARGE_CREATE(2, "发起充值微信支付");

    @EnumValue
    private int code;
    private String desc;

    BusinessType(int code, String desc) {
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
    public static Optional<BusinessType> getEnum(Integer code){
        if (code!=null){
            for (BusinessType type:BusinessType.values()){
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
    public static Optional<BusinessType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (BusinessType type:BusinessType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
