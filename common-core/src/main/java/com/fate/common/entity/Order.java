package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_")
public class Order extends Model<Order> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 应用ID
     */
    @TableField("merchant_app_id")
    private Long merchantAppId;

    /**
     * C端用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * 收银用户ID
     */
    @TableField("checker")
    private Long checker;

    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 订单状态
     */
    @TableField("status")
    private OrderStatus status;

    /**
     * 优惠券ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 优惠券减免金额
     */
    @TableField("coupon_amount")
    private BigDecimal couponAmount;

    /**
     * 商品总数
     */
    @TableField("total_num")
    private Integer totalNum;

    /**
     * 购买商品名称
     */
    @TableField("goods_names")
    private String goodsNames;

    /**
     * 订单总价
     */
    @TableField("order_sum_amount")
    private BigDecimal orderSumAmount;

    /**
     * 折扣金额
     */
    @TableField("discount_amount")
    private BigDecimal discountAmount;

    /**
     * 实际支付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;

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

    public static final String SHOP_ID = "shop_id";

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String CHECKER = "checker";

    public static final String ORDER_NO = "order_no";

    public static final String STATUS = "status";

    public static final String COUPON_ID = "coupon_id";

    public static final String COUPON_AMOUNT = "coupon_amount";

    public static final String TOTAL_NUM = "total_num";

    public static final String ORDER_SUM_AMOUNT = "order_sum_amount";

    public static final String DISCOUNT_AMOUNT = "discount_amount";

    public static final String PAY_AMOUNT = "pay_amount";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
