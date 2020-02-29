package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: parent
 * @description: 通知tab数量
 * @author: chenyixin
 * @create: 2019-09-01 22:14
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeNumDto {
    @ApiModelProperty("未读通知数量")
    private Integer noticeCount;
    @ApiModelProperty("未读公告数量")
    private Integer announceCount;
    @ApiModelProperty("未读反馈数量")
    private Integer feedbackCount;
}
