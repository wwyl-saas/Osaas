package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.CardType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务卡项表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_card")
public class Card extends Model<Card> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

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
     * 次卡标注
     */
    @TableField("tag")
    private String tag;

    /**
     * 折扣金额
     */
    @TableField("discount")
    private Integer discount;

    /**
     * 剩余下发量
     */
    @TableField("residue_num")
    private Integer residueNum;

    /**
     * 下发总量
     */
    @TableField("issue_num")
    private Integer issueNum;

    /**
     * 次卡生效次数
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
     * 生效状态，1生效0关闭
     */
    @TableField("enable")
    private Boolean enable;

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

    public static final String TYPE = "type";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String TAG = "tag";

    public static final String DISCOUNT = "discount";

    public static final String RESIDUE_NUM = "residue_num";

    public static final String ISSUE_NUM = "issue_num";

    public static final String EFFECT_NUM = "effect_num";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String ENABLE = "enable";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
