package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-03 14:53
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @ApiModelProperty(notes = "token")
    private String token;
    @ApiModelProperty(notes = "用户信息")
    private MerchantUserDto user;
}
