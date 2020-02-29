package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fate.common.enums.FeedBackType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description: 反馈数据
 * @author: chenyixin
 * @create: 2019-09-01 21:01
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto implements Serializable {
    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("C端用户ID")
    private Long customerId;

    @ApiModelProperty("C端用户名称")
    private String customerName;

    @ApiModelProperty("C端用户头像")
    private String customerAvatar;

    @ApiModelProperty("反馈类型")
    private FeedBackType type;

    @ApiModelProperty("是否匿名")
    private Boolean anonymous;

    @ApiModelProperty("反馈信息")
    private String remark;

    @ApiModelProperty("反馈图片ID,限制3个")
    private List<String> pictureList;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="MM-dd HH:mm")
    private LocalDateTime createTime;

    @ApiModelProperty("是否已读")
    private Boolean isRead;
}
