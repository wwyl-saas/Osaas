package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum CustomerSource {
    UNKNOW(0, "无来源"),
    WX_ONLINE(1, "微信激活"),
    ADMIN_ADD(2, "后台录入"),
    OLD_IMPORT(3, "外部导入");

    @EnumValue
    private Integer code;
    private String desc;

    CustomerSource(Integer code, String desc) {
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
    public static Optional<CustomerSource> getEnum(Integer code){
        if (code!=null){
            for (CustomerSource type:CustomerSource.values()){
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
    public static Optional<CustomerSource> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (CustomerSource type:CustomerSource.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
