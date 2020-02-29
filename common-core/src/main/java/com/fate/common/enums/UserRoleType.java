package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public enum UserRoleType {
    MANAGER(0, "总经理"),
    MANAGER_ASSISTANT(1, "总经理助理"),
    SHOP_OWNER(2, "店长"),
    SHOP_CHECKER(3, "副店长"),
    SHOP_WORKER(4, "店员"),
    SHOP_ASSISTANT(5, "助理");

    @EnumValue
    private int code;
    private String desc;

    UserRoleType(int code, String desc) {
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
    public static Optional<UserRoleType> getEnum(Integer code){
        if (code!=null){
            for (UserRoleType type: UserRoleType.values()){
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
    public static Optional<UserRoleType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (UserRoleType type: UserRoleType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
