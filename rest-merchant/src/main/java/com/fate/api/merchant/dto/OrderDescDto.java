package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 订单Dto
 * @author: chenyixin
 * @create: 2019-05-26 12:52
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDescDto implements Serializable {
    @ApiModelProperty(notes = "订单详情id")
    private Long id;
    @ApiModelProperty(notes = "商品名称")
    private String name;
    @ApiModelProperty(notes = "简图URL")
    private String briefPicUrl;
    @ApiModelProperty(notes = "商品数量")
    private Integer goodsNum;
    @ApiModelProperty(notes = "商品价格")
    private BigDecimal counterPrice;
}
