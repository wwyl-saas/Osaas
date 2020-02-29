package com.fate.api.merchant.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-05-06 15:15
 **/
@Data
public class GoodsSpecificationDto implements Serializable {
    private Integer gsId;

    private Integer goodsId;

    private Integer specificationId;

    private String value;

    private String picUrl;

    private String specificationName;
}
