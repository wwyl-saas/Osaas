package com.fate.api.merchant.query;

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
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("短信")
    private String smsCode;
}
