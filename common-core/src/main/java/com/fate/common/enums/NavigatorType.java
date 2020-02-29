package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description: 商户端首页导航
 * @author: chenyixin
 * @create: 2019-07-19 14:42
 **/
public enum NavigatorType {
    APPOINTMENT(1, "预约"),
    SETTLE(2, "结算"),
    STATISTIC(3, "统计"),
    EMPLOYEE(4, "员工"),
    MEMBER(5, "会员"),
    ACHIEVEMENT(6, "排行"),
    NOTICE(7, "通知");

    @EnumValue
    private int code;
    private String desc;

    NavigatorType(int code, String desc) {
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
    public static Optional<NavigatorType> getEnum(Integer code){
        if (code!=null){
            for (NavigatorType type: NavigatorType.values()){
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
    public static Optional<NavigatorType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (NavigatorType type: NavigatorType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
