package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.ChargeStatus;
import com.fate.common.enums.ChargeType;
import com.fate.common.enums.ConsumeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员充值表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_charge")
public class CustomerCharge extends Model<CustomerCharge> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 应用ID
     */
    @TableField("merchant_app_id")
    private Long merchantAppId;

    /**
     * 商铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * C端用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 充值类型
     */
    @TableField("charge_type")
    private ChargeType chargeType;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 返赠金额
     */
    @TableField("gift_amount")
    private BigDecimal giftAmount;

    /**
     * 充值状态
     */
    @TableField("charge_status")
    private ChargeStatus chargeStatus;

    /**
     * 操作用户ID
     */
    @TableField("operator_id")
    private Long operatorId;

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

    @Version
    @TableField("version")
    private Integer version;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String SHOP_ID = "shop_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String CHARGE_TYPE = "charge_type";

    public static final String AMOUNT = "amount";

    public static final String GIFT_AMOUNT = "gift_amount";

    public static final String CHARGE_STATUS = "charge_status";

    public static final String OPERATOR_ID = "operator_id";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
