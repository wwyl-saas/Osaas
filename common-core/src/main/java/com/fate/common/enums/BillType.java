package com.fate.common.enums;

import java.util.Optional;

/**
 * 账单类型
 */
public enum BillType {
    ALL(0),
    CONSUME(1),
    RECHARGE(2);

    private int code;

    BillType(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    /**
     * 根据序号返回枚举类型
     * @param code
     * @return
     */
    public static Optional<BillType> getEnum(Integer code){
        if (code!=null){
            for (BillType type:BillType.values()){
                if (code.equals(type.getCode())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
