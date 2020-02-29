package com.fate.api.merchant.dto;

import com.fate.common.enums.ConsumeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerConsumeDto  implements Serializable {


    @ApiModelProperty("商铺ID")
    private Long shopId;


    @ApiModelProperty("商铺用户ID")
    private Long merchantUserId;


    @ApiModelProperty("用户ID")
    private Long customerId;


    @ApiModelProperty("订单ID")
    private Long orderId;


    @ApiModelProperty("消费描述")
    private String description;


    @ApiModelProperty("消费类型")
    private ConsumeType consumeType;


    @ApiModelProperty("消费金额")
    private BigDecimal amount;


    @ApiModelProperty("消费时间")
    private LocalDateTime createTime;
}
