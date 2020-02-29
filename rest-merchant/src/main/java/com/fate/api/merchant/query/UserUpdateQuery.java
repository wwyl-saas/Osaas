package com.fate.api.merchant.query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-08-06 15:50
 **/
@Data
@ApiModel
public class UserUpdateQuery {
    @ApiModelProperty("艺名")
    private String nickname;
    @ApiModelProperty("用户照片")
    private String profileUrl;
    @ApiModelProperty("用户简介")
    private String userBrief;
    @ApiModelProperty("是否接受预约")
    private Boolean ifAppointment;
}
