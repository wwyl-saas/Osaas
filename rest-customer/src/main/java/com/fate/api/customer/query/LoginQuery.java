package com.fate.api.customer.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: parent
 * @description:授权登录
 * @author: chenyixin
 * @create: 2019-06-21 11:57
 **/
@Data
@ApiModel
public class LoginQuery {
    @NotBlank
    @ApiModelProperty("微信登录code")
    private String loginCode;
    @NotBlank
    @ApiModelProperty("签名")
    private String signature;
    @NotBlank
    @ApiModelProperty("原始数据")
    private String rawData;
    @NotBlank
    @ApiModelProperty("加密数据")
    private String encryptedData;
    @NotBlank
    @ApiModelProperty("偏移量")
    private String iv;
}
