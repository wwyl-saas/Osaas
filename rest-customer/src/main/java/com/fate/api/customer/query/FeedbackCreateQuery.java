package com.fate.api.customer.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fate.common.enums.FeedBackType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: parent
 * @description: 创建反馈
 * @author: chenyixin
 * @create: 2019-06-17 13:16
 **/
@ApiModel
@Data
public class FeedbackCreateQuery implements Serializable {
    @NotNull
    @ApiModelProperty(notes = "反馈类型")
    private FeedBackType type;

    @ApiModelProperty(notes = " 是否匿名")
    private Boolean anonymous=false;
    @NotBlank
    @ApiModelProperty(notes = "反馈信息")
    private String remark;

    @ApiModelProperty(notes = "反馈图片ID,限制3个")
    private String pictureUrls;


}
