package com.fate.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 操作类型
 */
public enum ActionType {
    CANCEL(0),
    FINISH(1);

    private int code;

    ActionType(int code) {
        this.code = code;
    }
    @JsonValue
    public Integer getCode() {
        return code;
    }
}
