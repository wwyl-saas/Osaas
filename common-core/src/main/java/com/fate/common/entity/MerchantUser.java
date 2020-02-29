package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.UserRoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户用户表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_user")
public class MerchantUser extends Model<MerchantUser> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 用户电话
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 用户姓名
     */
    @TableField("name")
    private String name;

    /**
     * 用户姓名
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 用户头像
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户照片
     */
    @TableField("profile_url")
    private String profileUrl;

    /**
     * 微信小程序openID
     */
    @TableField("wx_ap_openid")
    private String wxApOpenid;

    /**
     * 微信公众号openID
     */
    @TableField("wx_mp_openid")
    private String wxMpOpenid;

    /**
     * 微信unionID
     */
    @TableField("wx_unionid")
    private String wxUnionid;

    /**
     * 用户密码
     */
    @TableField("password")
    private String password;

    /**
     * 岗位职称ID
     */
    @TableField("post_title_id")
    private Long postTitleId;

    /**
     * 是否有效，针对预约
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 是否接受预约
     */
    @TableField("if_appointment")
    private Boolean ifAppointment;

    /**
     * 用户简介
     */
    @TableField("user_brief")
    private String userBrief;

    /**
     * 综合评分(100分制）
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 姓名拼音
     */
    @TableField("ping_yin")
    private String pingYin;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;


    /**
     * 最后登陆时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;


    /**
     * 角色类型
     */
    @TableField(exist = false)
    private UserRoleType roleType;

    /**
     * 所属店铺id
     */
    @TableField(exist = false)
    private Long shopId;

    /**
     *所属店铺的名称
     */
    @TableField(exist = false)
    private String shopName;





    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MOBILE = "mobile";

    public static final String NAME = "name";

    public static final String AVATAR_URL = "avatar_url";

    public static final String PROFILE_URL = "profile_url";

    public static final String WX_AP_OPENID = "wx_ap_openid";

    public static final String WX_MP_OPENID = "wx_mp_openid";

    public static final String WX_UNIONID = "wx_unionid";

    public static final String PASSWORD = "password";

    public static final String POST_TITLE_ID = "post_title_id";

    public static final String ENABLED = "enabled";

    public static final String IF_APPOINTMENT = "if_appointment";

    public static final String USER_BRIEF = "user_brief";

    public static final String GRADE = "grade";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String LAST_LOGIN_TIME = "last_login_time";

    public static final String PING_YIN = "ping_yin";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
