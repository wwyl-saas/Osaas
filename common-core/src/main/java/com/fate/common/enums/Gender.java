package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-08 14:19
 **/
public enum  Gender{
    UNKNOW(0, "未知"),
    MAN(1, "先生"),
    WOMAN(2, "女士");

    @EnumValue
    private int code;
    private String desc;

    Gender(int code, String desc) {
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
    public static Gender getEnum(Integer code){
        if (code!=null){
            for (Gender type:Gender.values()){
                if (code.equals(type.getCode())){
                    return type;
                }
            }
        }
        return UNKNOW;
    }

    /**
     * 根据描述返回枚举类型
     * @param desc
     * @return
     */
    public static Gender getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (Gender type:Gender.values()){
                if (desc.equals(type.getDesc())){
                    return type;
                }
            }
        }
        return UNKNOW;
    }
}
