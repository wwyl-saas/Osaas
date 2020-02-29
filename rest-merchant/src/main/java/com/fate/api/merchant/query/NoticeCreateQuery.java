package com.fate.api.merchant.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: parent
 * @description: 内部通知创建参数
 * @author:
 * @create: 2019-07-22 14:19
 **/
@ApiModel
@Data
public class NoticeCreateQuery {

    /**
     * 商户用户ID
     */
    @ApiModelProperty("商户用户Id，多个以逗号隔开")
    @NotNull
    private String merchantUserIds;

    /**
     * 通知类型
     */
    @ApiModelProperty("通知类型")
    @NotNull
    private Integer type;

    /**
     * 消息主题
     */
    @ApiModelProperty("消息主题")
    @NotNull
    private String subject;

    /**
     * 消息内容html格式
     */
    @ApiModelProperty("消息内容")
    @NotNull
    private String content;











}
