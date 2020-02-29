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
 * @description: 首页数据
 * @author: chenyixin
 * @create: 2019-08-23 23:00
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataAttributeDto implements Serializable {
    @ApiModelProperty("属性id")
    private Long id;
    @ApiModelProperty("属性名称")
    private String name;
    @ApiModelProperty("属性值")
    private String value;
    @ApiModelProperty("属性单位")
    private String unit;
    @ApiModelProperty("跳转页URL")
    private String url;
}
