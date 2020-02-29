package com.fate.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * @program: parent
 * @description: 媒体类型
 * @author: chenyixin
 * @create: 2019-06-11 18:16
 **/
public enum  MediaType {
    PICTURE(0, "图片"),
    VIDEO(1, "短视频");

    @EnumValue
    private int code;
    private String desc;

    MediaType(int code, String desc) {
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
    public static Optional<MediaType> getEnum(Integer code){
        if (code!=null){
            for (MediaType type:MediaType.values()){
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
    public static Optional<MediaType> getEnum(String desc){
        if (StringUtils.isNotBlank(desc)){
            for (MediaType type:MediaType.values()){
                if (desc.equals(type.getDesc())){
                    return Optional.of(type);
                }
            }
        }
        return Optional.empty();
    }
}
