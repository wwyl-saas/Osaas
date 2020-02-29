package com.fate.api.merchant.dto;

import com.fate.common.enums.UserRoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 用户个人中心
 * @author: chenyixin
 * @create: 2019-07-29 13:36
 **/

@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCenterDto implements Serializable {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户电话")
    private String mobile;
    @ApiModelProperty("用户姓名")
    private String name;
    @ApiModelProperty("用户艺名")
    private String nickname;
    @ApiModelProperty("用户头像")
    private String avatarUrl;
    @ApiModelProperty("用户照片")
    private String profileUrl;
    @ApiModelProperty("岗位职责")
    private String postTitle;
    @ApiModelProperty("角色")
    private UserRoleType roleType;
    @ApiModelProperty("用户简介")
    private String userBrief;
    @ApiModelProperty("综合评分")
    private String gradeString;
    @ApiModelProperty("评论数量")
    private Integer commentCount;
    @ApiModelProperty("部分头像")
    List<String> commentAvatarUrls;
    @ApiModelProperty("是否接受预约")
    private Boolean ifAppointment;
}
