package com.fate.api.merchant.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantDto  implements Serializable {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("商户name")
    private String name;


}
