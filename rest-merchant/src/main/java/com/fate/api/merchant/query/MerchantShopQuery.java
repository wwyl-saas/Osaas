package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @program: parent
 * @description:
 * @author:
 * @create:
 **/
@ApiModel
@Data
public class MerchantShopQuery {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("门店名称")
    @NotBlank
    private String name;

    @ApiModelProperty("联系电话")
    @NotBlank
    private String telephone;

    @ApiModelProperty("门店地址")
    @NotBlank
    private String address;

    @NotBlank
    @ApiModelProperty("门店照片")
    private String picture;

    @ApiModelProperty("经度坐标")
    @NotNull
    private Double longitude;

    @ApiModelProperty("纬度坐标")
    @NotNull
    private Double latitude;

    @ApiModelProperty("是否有效")
    @NotNull
    private Boolean enabled;



}
