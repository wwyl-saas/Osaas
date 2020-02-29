package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.CardStatus;
import com.fate.common.enums.CardType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * C端用户卡项表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_card")
public class CustomerCard extends Model<CustomerCard> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * C端用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 来源应用ID
     */
    @TableField("merchant_app_id")
    private Long merchantAppId;

    @TableField("card_id")
    private Long cardId;

    /**
     * 次卡类型
     */
    @TableField("type")
    private CardType type;

    /**
     * 次卡名称
     */
    @TableField("name")
    private String name;

    /**
     * 次卡详情
     */
    @TableField("description")
    private String description;

    /**
     * 此卡标注
     */
    @TableField("tag")
    private String tag;

    /**
     * 折扣金额
     */
    @TableField("discount")
    private Integer discount;

    /**
     * 剩余次数
     */
    @TableField("residue_num")
    private Integer residueNum;

    /**
     * 次卡有效次数
     */
    @TableField("effect_num")
    private Integer effectNum;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDate endTime;

    /**
     * 状态
     */
    @TableField("status")
    private CardStatus status;

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

    public static final String CUSTOMER_ID = "customer_id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String CARD_ID = "card_id";

    public static final String TYPE = "type";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String TAG = "tag";

    public static final String DISCOUNT = "discount";

    public static final String RESIDUE_NUM = "residue_num";

    public static final String EFFECT_NUM = "effect_num";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
