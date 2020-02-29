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
 * @description: 预约-服务列表
 * @author: chenyixin
 * @create: 2019-06-06 12:11
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSelectDto implements Serializable {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty(notes = "当前是否匹配，true为匹配")
    private Boolean match;
}
