package com.fate.api.customer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: parent
 * @description: 商户信息
 * @author: chenyixin
 * @create: 2019-05-23 08:47
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantIndexDto implements Serializable {
    @ApiModelProperty(notes = "商户名称")
    private String name;
    @ApiModelProperty(notes = "商户介绍")
    private String desc;
    @ApiModelProperty(notes = "营业时间")
    private String workTime;
    @ApiModelProperty(notes = "商铺信息")
    private List<MerchantShopDto> shops;
}
