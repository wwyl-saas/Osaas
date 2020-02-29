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
public class MemberWelfareDto implements Serializable {

    @ApiModelProperty(notes = "福利ID")
    private Long id;

    @ApiModelProperty(notes = "商户ID")
    private Long merchantId;

    @ApiModelProperty(notes = "会员卡ID")
    private Long memberId;

    @ApiModelProperty(notes = "福利类型编号")
    private int code;

    @ApiModelProperty(notes = "福利描述")
    private String desc;

    @ApiModelProperty(notes = "福利图标")
    private String icon;

    @ApiModelProperty(notes = "福利val")
    private String welfareValue;


}
