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
 * @description: 统计数据通用
 * @author: chenyixin
 * @create: 2019-08-25 14:54
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDataDto implements Serializable {
    @ApiModelProperty("数据标识")
    private String dataIndex;
    @ApiModelProperty("数据值")
    private BigDecimal dataValue;
}
