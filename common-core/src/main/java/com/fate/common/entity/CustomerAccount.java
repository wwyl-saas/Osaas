package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.MemberLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * C端用户账户表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_account")
public class CustomerAccount extends Model<CustomerAccount> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 商户会员级别Id
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 会员级别
     */
    @TableField("member_level")
    private MemberLevel memberLevel;

    /**
     * 消费积分
     */
    @TableField("consume_score")
    private Integer consumeScore;

    /**
     * 消费次数
     */
    @TableField("consume_num")
    private Integer consumeNum;

    /**
     * 消费次数
     */
    @TableField("charge_num")
    private Integer chargeNum;

    /**
     * 账户余额
     */
    @TableField("balance")
    private BigDecimal balance;

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
     * 累计充值金额
     */
    @TableField("cumulative_recharge")
    private BigDecimal cumulativeRecharge;


    /**
     * 累计消费金额
     */
    @TableField("cumulative_consume")
    private BigDecimal cumulativeConsume;

    /**
     * 微信会员卡号
     */
    @TableField("wx_card_code")
    private String wxCardCode;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String MEMBER_LEVEL = "member_level";

    public static final String CONSUME_SCORE = "consume_score";

    public static final String CONSUME_NUM = "consume_num";

    public static final String CHARGE_NUM = "charge_num";

    public static final String BALANCE = "balance";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String WX_CARD_CODE = "wx_card_code";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
