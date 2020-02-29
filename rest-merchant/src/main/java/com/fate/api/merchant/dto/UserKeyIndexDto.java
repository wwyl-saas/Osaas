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
 * @description: 关键指标
 * @author: chenyixin
 * @create: 2019-08-24 01:51
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserKeyIndexDto implements Serializable {
    @ApiModelProperty("累计销售")
    private BigDecimal rechargeAmount;
    @ApiModelProperty("累计服务")
    private BigDecimal serviceTime;
    @ApiModelProperty("累计营收")
    private BigDecimal serviceAmount;
}
