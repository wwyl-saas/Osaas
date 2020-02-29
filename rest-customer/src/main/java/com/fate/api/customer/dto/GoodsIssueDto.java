package com.fate.api.customer.dto;

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
 * @create: 2019-06-12 17:58
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsIssueDto {

    @ApiModelProperty("问题")
    private String question;

    @ApiModelProperty("回答")
    private String answer;

}
