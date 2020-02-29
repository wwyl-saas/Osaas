package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @program: parent
 * @description: 首页商品模块
 * @author: chenyixin
 * @create: 2019-07-18 15:57
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexGoodsDto {
    @ApiModelProperty("模块名称")
    private String name;
    @ApiModelProperty("模块背景图")
    private String img;
    @ApiModelProperty("模块简介")
    private String[] descArray;
    @ApiModelProperty("商品列表")
    private List<GoodsDto> goodsList;
}
