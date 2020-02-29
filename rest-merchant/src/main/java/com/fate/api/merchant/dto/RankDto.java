package com.fate.api.merchant.dto;

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
 * @description: 绩效对象
 * @author: chenyixin
 * @create: 2019-07-23 15:30
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankDto implements Serializable {
    @ApiModelProperty(value = "用户Id")
    private Long merchantUserId;
    @ApiModelProperty(value = "用户姓名")
    private String merchantUserName;
    @ApiModelProperty(value = "名次")
    private Integer rank;
    @ApiModelProperty(value = "绩效值")
    private BigDecimal value;
}
