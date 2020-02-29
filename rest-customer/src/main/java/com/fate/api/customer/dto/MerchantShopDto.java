package com.fate.api.customer.dto;

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
 * @description: 商铺信息
 * @author: chenyixin
 * @create: 2019-05-23 08:53
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantShopDto implements Serializable {
    @ApiModelProperty(notes = "门店ID")
    private Long id;
    @ApiModelProperty(notes = "门店名称")
    private String name;
    @ApiModelProperty(notes = "门店电话")
    private String telephone;
    @ApiModelProperty(notes = "门店地址")
    private String address;
    @ApiModelProperty(notes = "门店图片")
    private String picture;
    @ApiModelProperty(notes = "门店经度")
    private Double longitude;
    @ApiModelProperty(notes = "门店维度")
    private Double latitude;
    @ApiModelProperty(notes = "门店距离，单位：千米")
    private BigDecimal distance;
    @ApiModelProperty(notes = "是否最近")
    private boolean ifNearest;
}
