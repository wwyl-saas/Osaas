package com.fate.api.merchant.dto;

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
 * @description: 员工索引列表
 * @author: chenyixin
 * @create: 2019-08-15 10:55
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUserIndexDto implements Serializable {
    @ApiModelProperty(value = "索引")
    private String indexLetter;
    @ApiModelProperty(value = "用户列表")
    private List<MerchantUserDto> userList;
}
