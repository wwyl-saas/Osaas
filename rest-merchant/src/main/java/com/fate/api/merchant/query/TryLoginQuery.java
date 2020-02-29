package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: parent
 * @description: 尝试登录
 * @author: chenyixin
 * @create: 2019-06-21 11:36
 **/
@Data
@ApiModel
public class TryLoginQuery {
    @NotNull
    @ApiModelProperty("微信登录code")
    private String loginCode;
}
