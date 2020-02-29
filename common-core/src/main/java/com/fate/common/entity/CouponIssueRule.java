package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.EffectRule;
import com.fate.common.enums.RuleRelationType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 卡券发放规则表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_coupon_issue_rule")
public class CouponIssueRule extends Model<CouponIssueRule> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 优惠券ID
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     * 规则类型
     */
    @TableField("rule_type")
    private EffectRule ruleType;

    /**
     * 条件关系
     */
    @TableField("relation")
    private RuleRelationType relation;

    /**
     * 参考值，in和notin不能超过12个元素
     */
    @TableField("rule_value")
    private String ruleValue;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String COUPON_ID = "coupon_id";

    public static final String RULE_TYPE = "rule_type";

    public static final String RELATION = "relation";

    public static final String RULE_VALUE = "rule_value";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
