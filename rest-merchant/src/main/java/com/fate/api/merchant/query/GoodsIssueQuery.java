package com.fate.api.merchant.query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @program: parent
 * @description:
 * @author:
 * @create: 2019-07-22 14:40
 **/
@ApiModel
@Data
public class GoodsIssueQuery {

    @ApiModelProperty("商品ID")
    private Long goodsId;

    @ApiModelProperty("问题")
    @NotBlank
    private String question;

    @ApiModelProperty("回答")
    @NotBlank
    private String answer;




}
