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
 * @description: 门槛值相关
 * @author:
 * @create: 2019-06-11 13:37
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberThresholdDto implements Serializable {

    @ApiModelProperty("门槛值val")
    private String thresholdValue;

    @ApiModelProperty("门槛值类型")
    private int code;

    @ApiModelProperty("门槛值描述")
    private String desc;


}
