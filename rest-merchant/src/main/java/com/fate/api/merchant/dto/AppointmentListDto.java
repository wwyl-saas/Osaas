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
 * @description: 预约列表预先统计
 * @author: chenyixin
 * @create: 2019-08-20 15:49
 **/

@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentListDto implements Serializable {
    @ApiModelProperty("预约总数")
    private Integer total;
    @ApiModelProperty("待确认")
    private Integer waitingConfirm;
    @ApiModelProperty("已确认")
    private Integer confirmed;
    @ApiModelProperty("已取消")
    private Integer canceled;
    @ApiModelProperty("分页列表")
    private PageDto<AppointmentDto> pageList;
}
