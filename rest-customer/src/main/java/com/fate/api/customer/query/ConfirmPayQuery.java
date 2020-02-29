package com.fate.api.customer.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: parent
 * @description: 确认支付
 * @author: chenyixin
 * @create: 2019-06-21 15:09
 **/
@ApiModel
@Data
public class ConfirmPayQuery {
    @NotNull
    @ApiModelProperty("订单ID")
    private Long orderId;
    @NotBlank
    @ApiModelProperty("表单ID")
    private String formId;
}
