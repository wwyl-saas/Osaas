package com.fate.api.customer.dto;

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
 * @description: 分类页
 * @author: chenyixin
 * @create: 2019-06-04 23:28
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    @ApiModelProperty(notes = "分类ID")
    private Long id;
    @ApiModelProperty(notes = "分类名称")
    private String name;
    @ApiModelProperty(notes = "分类ICON")
    private String icon;
    @ApiModelProperty(notes = "分类ICON-选择状态")
    private String iconOn;
    @ApiModelProperty(notes = "分类商品列表")
    private List<GoodsDto> goodsList;
}
