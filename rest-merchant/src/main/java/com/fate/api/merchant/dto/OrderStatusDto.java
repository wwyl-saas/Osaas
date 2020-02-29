package com.fate.api.merchant.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: songjinghuan
 * @create: 2019-06-09 12:45
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDto implements Serializable {
    @ApiModelProperty(notes = "订单状态编码")
    private  Integer code;
    @ApiModelProperty(notes = "订单状态名字")
    private String desc;

}
