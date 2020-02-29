package com.fate.api.merchant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: parent
 * @description: 评论dto
 * @author: chenyixin
 * @create: 2019-06-09 17:30
 **/
@Data
@ApiModel
public class CommentDto implements Serializable {
    @ApiModelProperty("顾客名称")
    private String customerName;
    @ApiModelProperty("顾客头像")
    private String avatar;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;
    @ApiModelProperty("评论")
    private String remark;
    @ApiModelProperty("评价附图列表")
    private List<String> pictureList;
    @ApiModelProperty("评分")
    private String gradeString;

}
