package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.Gender;
import com.fate.common.enums.MemberLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * C端用户表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer")
public class Customer extends Model<Customer> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 微信unionId
     */
    @TableField("wx_unoinid")
    private String wxUnoinid;

    /**
     * 用户名称
     */
    @TableField("name")
    private String name;

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
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 来源
     */
    @TableField("source")
    private CustomerSource source;

    /**
     * 最后登录时间
     */
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

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

    //会员等级，临时字段
    @TableField(exist = false)
    private  MemberLevel  memberLevel;

    //会员积分，临时字段
    @TableField(exist = false)
    private Integer consumeScore;

    //会员余额，临时字段
    @TableField(exist = false)
    private BigDecimal balance;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MOBILE = "mobile";

    public static final String WX_UNOINID = "wx_unoinid";

    public static final String NAME = "name";

    public static final String NICKNAME = "nickname";

    public static final String CITY_ID = "city_id";

    public static final String GENDER = "gender";

    public static final String AVATAR = "avatar";

    public static final String BIRTHDAY = "birthday";

    public static final String LAST_LOGIN_TIME = "last_login_time";

    public static final String SOURCE = "source";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
