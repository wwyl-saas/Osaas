package com.fate.api.merchant.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointStatusDto implements Serializable {
    @ApiModelProperty(notes = "预约状态编码")
    private  Integer code;
    @ApiModelProperty(notes = "预约状态名字")
    private String desc;
}
