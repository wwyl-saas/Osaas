package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class CategoryQuery {
    @ApiModelProperty("商品id")
    private Long  id;

    @ApiModelProperty("商品名字")
    @NotBlank
    private String name;

    @NotNull
    @ApiModelProperty("商品展示排序")
    private Integer sortOrder;

    @ApiModelProperty("商品图标")
    private String icon;

    @ApiModelProperty("商品图标")
    private String iconOn;

    @ApiModelProperty("是否启用")
    @NotNull
    private Boolean enable;
}
