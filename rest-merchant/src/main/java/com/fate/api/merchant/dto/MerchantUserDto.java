package com.fate.api.merchant.dto;


import com.fate.common.enums.UserRoleType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商户用户表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUserDto implements Serializable {
    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 商户ID
     */
    @ApiModelProperty("商户ID")
    private Long merchantId;

    /**
     * 用户电话
     */
    @ApiModelProperty("用户电话")
    private String mobile;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String name;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户艺名")
    private String nickname;

    /**
     * 用户姓名拼音
     */
    @ApiModelProperty("用户艺名")
    private String pingYin;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String avatarUrl;

    /**
     * 用户照片
     */
    @ApiModelProperty("用户照片")
    private String profileUrl;

    /**
     * 岗位职称
     */
    @ApiModelProperty("岗位职责")
    private String postTitle;

    /**
     * 岗位职称
     */
    @ApiModelProperty("岗位职责ID")
    private Long postTitleId;

    /**
     * 角色
     */
    @ApiModelProperty("角色")
    private UserRoleType roleType;

    /**
     * 角色
     */
    @ApiModelProperty("角色码")
    private Integer roleTypeCode;

    /**
     * 是否有效，针对预约
     */
    @ApiModelProperty("是否有效，针对预约")
    private Boolean enabled;

    /**
     * 是否接受预约
     */
    @ApiModelProperty("是否接受预约")
    private Boolean ifAppointment;

    /**
     * 用户简介
     */
    @ApiModelProperty("用户简介")
    private String userBrief;

    /**
     * 综合评分(100分制）
     */
    @ApiModelProperty("综合评分")
    private Integer grade;

    @ApiModelProperty("可服务商品列表")
    List<Long> goodsIds;


    /**
     * 店铺id
     */
    @ApiModelProperty("店铺ID")
    private Long shopId;

    /**
     * 店铺name
     */
    @ApiModelProperty("店铺名字")
    private String shopName;
}

