package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public enum AppointmentStatus {
    WAITING_CONFIRM(0,"待确认"),
    CONFIRMED(1,"已确认"),
    CANCELED(2,"已取消"),
    EXPIRED(3,"已过期");


    @EnumValue
    private final Integer code;
    private String desc;

    AppointmentStatus(int code,String desc) {
        this.code=code;
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
    public static Optional<AppointmentStatus> getEnum(Integer code){
        for (AppointmentStatus type:AppointmentStatus.values()){
            if (type.getCode().equals(code)){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    /**
     * 根据描述返回枚举类型
     * @param desc
     * @return
     */
    public static Optional<AppointmentStatus> getEnum(String desc){
        for (AppointmentStatus type:AppointmentStatus.values()){
            if (type.getDesc().equals(desc)){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
