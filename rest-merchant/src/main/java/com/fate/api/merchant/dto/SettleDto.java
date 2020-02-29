package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-08-07 19:42
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleDto implements Serializable {
    @ApiModelProperty(notes = "分类列表")
    private List<CategoryDto> categorys;
    @ApiModelProperty(notes = "快捷商品ID")
    private List<Long> bestSell;
}
