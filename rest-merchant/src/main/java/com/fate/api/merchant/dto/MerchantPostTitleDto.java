package com.fate.api.merchant.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * @program: parent
 * @description:
 * @author:
 * @create:
 **/
@ApiModel
@Data
public class MerchantPostTitleDto {
    @ApiModelProperty("职称id")
    private Long  id;

    @ApiModelProperty("职称名称")
    private String name;

    @ApiModelProperty("职称描述")
    private String description;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;







}
