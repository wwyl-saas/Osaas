package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum MemberWelfareType {
    GOODS_DISCOUNT(0, "商品折扣","welfare0.png"),
    SERVICE_DISCOUNT(1, "服务折扣","welfare1.png"),
    COUPON(2, "优惠礼包","welfare2.png"),
    BIRTHDAY_DISCOUNT(3, "生日特惠","welfare3.png");

    @EnumValue
    private int code;
    private String desc;
    private String icon;

    MemberWelfareType(int code, String desc, String icon) {
        this.code = code;
        this.desc = desc;
        this.icon = icon;
    }

    public Integer getCode() {
        return code;
    }
    @JsonValue
    public String getDesc() {
        return desc;
    }
    public String getIcon(){
        return icon;
    }
    /**
     * 根据序号返回枚举类型
     * @param code
     * @return
     */
    public static Optional<MemberWelfareType> getEnum(Integer code){
        if (code!=null){
            for (MemberWelfareType type: MemberWelfareType.values()){
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
    public static Optional<MemberWelfareType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (MemberWelfareType type: MemberWelfareType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
