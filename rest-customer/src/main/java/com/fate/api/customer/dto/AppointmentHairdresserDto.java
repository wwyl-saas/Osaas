package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 预约-技师
 * @author: chenyixin
 * @create: 2019-06-06 12:09
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentHairdresserDto implements Serializable {
    @ApiModelProperty(notes = "技师ID")
    private Long merchantUserId;
    @ApiModelProperty(notes = "技师名称")
    private String name;
    @ApiModelProperty(notes = "技师头衔")
    private String title;
    @ApiModelProperty(notes = "技师头像")
    private String portrait;
    @ApiModelProperty(notes = "技师介绍")
    private String introduce;
    @ApiModelProperty(notes = "技师好评率")
    private Integer favorableRate;
    @ApiModelProperty(notes = "当前是否匹配，true为匹配")
    private Boolean match;
    @ApiModelProperty(notes = "排序，逆序")
    private Integer order;
}
