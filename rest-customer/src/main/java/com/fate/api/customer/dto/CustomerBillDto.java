package com.fate.api.customer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description: 会员账单
 * @author: chenyixin
 * @create: 2019-08-08 17:30
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBillDto {
    @ApiModelProperty("记录ID")
    private Long id;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("正负符号")
    private Integer flag;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间")
    private LocalDateTime dateTime;

    @ApiModelProperty("商品描述")
    private String goodsDesc;

    @ApiModelProperty("交易描述")
    private String settleDesc;
}
