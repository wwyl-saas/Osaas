package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationType {
    H5(0,"h5网站"),
    WX_PUBLIC(1,"微信公众号"),
    WX_APPLET(2, "微信小程序");

    @EnumValue
    private final int code;
    private String desc;

    ApplicationType(int code,String desc) {
        this.code=code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    @JsonValue
    public String getDesc() {
        return desc;
    }
}
