package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description: 微信卡券
 * @author: chenyixin
 * @create: 2019-09-07 23:33
 **/
public enum  WxCardType {
    MEMBER_CARD(0, "会员卡"),
    GROUPON(1, "团购券"),
    CASH(2, "代金券"),
    DISCOUNT(3,"折扣券"),
    GIFT(4,"兑换券"),
    GENERAL_COUPON(5,"优惠券");

    @EnumValue
    private int code;
    private String desc;

    WxCardType(int code, String desc) {
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
    public static Optional<WxCardType> getEnum(Integer code){
        if (code!=null){
            for (WxCardType type:WxCardType.values()){
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
    public static Optional<WxCardType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (WxCardType type:WxCardType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
