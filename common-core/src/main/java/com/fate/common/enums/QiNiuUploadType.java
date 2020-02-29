package com.fate.common.enums;
/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-06-16 11:41
 **/
public enum  QiNiuUploadType {
    PATH(0, "文件路径"),
    ARRAY(1, "字节数组"),
    STREAM(2, "文件流");

    private int code;
    private String desc;

    QiNiuUploadType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
