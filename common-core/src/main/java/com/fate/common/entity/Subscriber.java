package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业公众号订阅者表（需商户用户订阅）
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_subscriber")
public class Subscriber extends Model<Subscriber> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 微信unionId
     */
    @TableField("wx_unoinid")
    private String wxUnoinid;

    /**
     * 微信openId
     */
    @TableField("wx_openid")
    private String wxOpenid;

    /**
     * 别名
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 所属城市
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 性别
     */
    @TableField("gender")
    private Gender gender;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 是否订阅
     */
    @TableField("subscribe")
    private Boolean subscribe;

    /**
     * 来源
     */
    @TableField("source")
    private String source;

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


    public static final String ID = "id";

    public static final String WX_UNOINID = "wx_unoinid";

    public static final String WX_OPENID = "wx_openid";

    public static final String NICKNAME = "nickname";

    public static final String CITY_ID = "city_id";

    public static final String GENDER = "gender";

    public static final String AVATAR = "avatar";

    public static final String SUBSCRIBE = "subscribe";

    public static final String SOURCE = "source";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
