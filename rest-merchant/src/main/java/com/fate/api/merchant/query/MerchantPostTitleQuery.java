package com.fate.api.merchant.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;



/**
 * @program: parent
 * @description:
 * @author:
 * @create:
 **/
@ApiModel
@Data
public class MerchantPostTitleQuery {

    @ApiModelProperty("职称id")
    private Long  id;

    @ApiModelProperty("职称名称")
    @NotBlank
    private String name;

    @NotBlank
    @ApiModelProperty("职称描述")
    private String desc;





}
