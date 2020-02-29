package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-03 14:50
 **/
@Data
@ApiModel
public class AdminLoginQuery {
    @NotBlank
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("密码")
    private String pwd;
    @ApiModelProperty("短信")
    private String smsCode;
}
