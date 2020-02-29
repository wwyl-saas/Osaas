package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: parent
 * @description: 通知数据
 * @author: chenyixin
 * @create: 2019-09-01 21:00
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto implements Serializable {

    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("通知类型")
    private Integer type;

    @ApiModelProperty("通知者")
    private String name;

    @ApiModelProperty("通知者头像")
    private String avatar;

    @ApiModelProperty("消息主题")
    private String subject;

    @ApiModelProperty("消息内容html格式")
    private String content;

    @ApiModelProperty("是否已读")
    private Boolean isRead;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "MM-dd HH:mm")
    private LocalDateTime createTime;
}
