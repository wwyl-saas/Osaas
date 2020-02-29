package com.fate.api.merchant.util.Excel;

import lombok.Data;

import java.util.Date;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Data
public class UploadData {
    private String string;
    private Date date;
    private Double doubleData;
}
