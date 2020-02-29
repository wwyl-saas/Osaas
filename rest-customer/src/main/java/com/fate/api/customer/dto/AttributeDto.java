package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: parent
 * @description: 属性
 * @author: chenyixin
 * @create: 2019-06-07 17:19
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto implements Serializable {
    @ApiModelProperty("属性名称")
    private String name;
    @ApiModelProperty("属性值")
    private String value;
    @ApiModelProperty("属性单位")
    private String unit;
}
