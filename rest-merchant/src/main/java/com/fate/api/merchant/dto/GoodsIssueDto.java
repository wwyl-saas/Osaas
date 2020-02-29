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
public class GoodsIssueDto implements Serializable {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("回答")
    private String answer;

}
