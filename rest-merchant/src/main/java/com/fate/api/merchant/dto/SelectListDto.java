package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 下拉列表
 * @author: chenyixin
 * @create: 2019-07-29 11:53
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectListDto implements Serializable {
    @ApiModelProperty("标识码")
    private Long code;
    @ApiModelProperty("名称")
    private String name;
}
