package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description: 首页指标
 * @author: chenyixin
 * @create: 2019-07-19 15:46
 **/
public enum StatisticType {
    EVALUATE(0,"会员评价","分"),//商户、门店、个人（精确划分）
    DATE_VISIT(1, "线上访问","人"),//商户
    DATE_CUSTOMER(2, "客户增量","人"),//商户
    DATE_MEMBER_INCREASE(3, "会员增量","人"),//商户
    DATA_MEMBER_RECHARGE(4, "充值会员","人"),//商户、门店、个人（部分到商户）
    DATE_APPOINTMENT(5, "预约服务","个"),//商户、门店、个人（部分到门店）
    DATE_ORDER(6, "交易订单","个"),//商户、门店、个人（精确划分）
    DATE_RECHARGE_FACT(7,"实际充值","元"),//商户、门店、个人（部分到商户）
    DATE_RECHARGE_GIFT(8,"充值返赠","元"),//商户、门店、个人（部分到商户）
    DATE_ACCOUNT_RECHARGE(9,"账面充值","元"),//商户、门店、个人（部分到商户）
    DATE_CONSUME_FACT(10, "实际收款","元"),//商户、门店、个人（精确划分）
    DATE_CONSUME_STORE(11, "储值消耗","元"),//商户、门店、个人（精确划分）
    DATE_CONSUME(12, "账面营业","元");//商户、门店、个人（精确划分）

    @EnumValue
    private int code;
    private String desc;
    private String unit;

    StatisticType(int code, String desc, String unit) {
        this.code = code;
        this.desc = desc;
        this.unit=unit;
    }

    public Integer getCode() {
        return code;
    }
    @JsonValue
    public String getDesc() {
        return desc;
    }

    public String getUnit() {
        return unit;
    }
    /**
     * 根据序号返回枚举类型
     * @param code
     * @return
     */
    public static Optional<StatisticType> getEnum(Integer code){
        if (code!=null){
            for (StatisticType type: StatisticType.values()){
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
    public static Optional<StatisticType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (StatisticType type: StatisticType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
