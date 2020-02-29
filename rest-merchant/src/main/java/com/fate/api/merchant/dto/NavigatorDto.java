package com.fate.api.merchant.dto;

import com.fate.common.entity.Navigator;
import com.fate.common.enums.NavigatorType;
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
 * @description: 导航对象
 * @author: chenyixin
 * @create: 2019-07-19 09:23
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NavigatorDto implements Serializable {
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("标签值")
    private Integer badge;
    @ApiModelProperty("名称")
    private NavigatorType name;
    @ApiModelProperty("URL")
    private String url;

}
