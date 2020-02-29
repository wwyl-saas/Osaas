package com.fate.api.merchant.dto;

import com.fate.common.enums.MemberLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCardDto implements Serializable {
    @ApiModelProperty(notes = "会员id")
    private Long id;

    @ApiModelProperty(notes = "商家id")
    private Long merchantId;

    @ApiModelProperty(notes = "会员级别编码")
    private Integer levelCode;

    @ApiModelProperty(notes = "会员级别描述")
    private String levelDesc;

    @ApiModelProperty(notes = "会员url")
    private String pictureUrl;

    @ApiModelProperty(notes = "会员url")
    private String wxPictureUrl;

    @ApiModelProperty(notes = "会员福利类型")
    private String welfareAllTypes;

    @ApiModelProperty(notes = "会员福利列表")
    private List<MemberWelfareDto> welfareAllTypesList;

    //@ApiModelProperty("会员阈值列表")
    //List<MemberThresholdDto> memberThresholdList;




}
