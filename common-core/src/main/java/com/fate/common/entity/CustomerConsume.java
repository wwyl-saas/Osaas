package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.ConsumeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员充值消费记录表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_consume_")
public class CustomerConsume extends Model<CustomerConsume> implements Serializable{

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
     * 订单
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 日志描述
     */
    @TableField("description")
    private String description;

    /**
     * 记录类型
     */
    @TableField("consume_type")
    private ConsumeType consumeType;

    /**
     * 金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    //消费门店，saas消费记录查询时新增SJH
    @TableField(exist = false)
    private String shopName;



    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String SHOP_ID = "shop_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String ORDER_ID = "order_id";

    public static final String DESC = "description";

    public static final String CONSUME_TYPE = "consume_type";

    public static final String AMOUNT = "amount";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
