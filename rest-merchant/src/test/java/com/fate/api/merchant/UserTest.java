package com.fate.api.merchant;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import groovy.transform.builder.Builder;
import lombok.Data;

@Data
@Builder
public class UserTest extends BaseRowModel {

    @ExcelProperty(value = "姓名",index = 0)
    private String name;

    @ExcelProperty(value = "密码",index = 1)
    private String password;

    @ExcelProperty(value = "年龄",index = 2)
    private Integer age;
}
