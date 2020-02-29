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
import java.time.LocalDateTime;

/**
 * <p>
 * 商户用户角色表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_user_role")
public class MerchantUserRole extends Model<MerchantUserRole> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 门店ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private UserRoleType roleType;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String SHOP_ID = "shop_id";

    public static final String USER_ID = "user_id";

    public static final String ROLE_TYPE = "role_type";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
