package com.fate.api.merchant.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 商品属性
 * @author: chenyixin
 * @create: 2019-05-06 10:02
 **/
@Getter
@Setter
public class GoodsAttribute implements Serializable {
    private String name;
    private String value;
}
