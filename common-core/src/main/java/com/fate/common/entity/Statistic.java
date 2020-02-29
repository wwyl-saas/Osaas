package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.StatisticType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户首页指标表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_statistic_")
public class Statistic extends Model<Statistic> {

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
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * 统计类型
     */
    @TableField("type")
    private StatisticType type;

    /**
     * 统计值
     */
    @TableField("data_value")
    private BigDecimal dataValue;

    /**
     * 数据日期
     */
    @TableField("data_date")
    private LocalDate dataDate;

    /**
     * 年度
     */
    @TableField("year")
    private Integer year;

    /**
     * 自然季度1,2,3,4
     */
    @TableField("quarter")
    private Integer quarter;

    /**
     * 自然月1-12
     */
    @TableField("month")
    private Integer month;

    /**
     * 自然周1-54
     */
    @TableField("week")
    private Integer week;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String SHOP_ID = "shop_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String TYPE = "type";

    public static final String DATA_VALUE = "data_value";

    public static final String DATA_DATE = "data_date";

    public static final String YEAR = "year";

    public static final String QUARTER = "quarter";

    public static final String MONTH = "month";

    public static final String WEEK = "week";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
