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

/**
 * <p>
 * 商户角色导航表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_role_navigator")
public class MerchantRoleNavigator extends Model<MerchantRoleNavigator> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 角色类型
     */
    @TableField("role_type")
    private UserRoleType roleType;

    /**
     * 导航类型
     */
    @TableField("navigator_id")
    private Long navigatorId;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String ROLE_TYPE = "role_type";

    public static final String NAVIGATOR_ID = "navigator_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
