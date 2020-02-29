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
 * @description: 公告
 * @author: chenyixin
 * @create: 2019-09-01 21:01
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnounceDto implements Serializable {
    @ApiModelProperty("Id")
    private Long id;

    @ApiModelProperty("商户ID")
    private Long merchantId;

    @ApiModelProperty("公告类型")
    private Integer type;

    @ApiModelProperty("公告名称")
    private String name;

    @ApiModelProperty("公告内容html格式")
    private String content;

    @ApiModelProperty("标签")
    private String tag;

    @ApiModelProperty("背景图")
    private String pictureUrl;

    @ApiModelProperty("是否开启")
    private Boolean isRead;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="MM-dd HH:mm")
    private LocalDateTime createTime;

    @ApiModelProperty("阅读数")
    private Integer readNum;
}
