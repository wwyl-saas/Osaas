package com.fate.common.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会员代付表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_another_pay")
public class PayForAnother extends Model<PayForAnother> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 来源应用ID
     */
    @TableField("application_id")
    private Long applicationId;

    /**
     * 代付用户ID
     */
    @TableField("pay_customer_id")
    private Long payCustomerId;


    /**
     * 消费记录ID
     */
    @TableField("consume_id")
    private Long consumeId;

    /**
     * 代付金额
     */
    @TableField("pay_amount")
    private BigDecimal payAmount;

    /**
     * 代付码
     */
    @TableField("pay_code")
    private Long payCode;

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

    public static final String MERCHANT_ID = "merchant_id";

    public static final String APPLICATION_ID = "application_id";

    public static final String PAY_CUSTOMER_ID = "pay_customer_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String CONSUME_ID = "consume_id";

    public static final String PAY_AMOUNT = "pay_amount";

    public static final String PAY_CODE = "pay_code";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
