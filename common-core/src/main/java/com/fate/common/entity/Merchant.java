package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant")
public class Merchant extends Model<Merchant> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户名称
     */
    @TableField("name")
    private String name;

    /**
     * 所属城市
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 联系电话
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 销售ID
     */
    @TableField("saler_id")
    private Integer salerId;

    /**
     * 渠道来源
     */
    @TableField("source")
    private Integer source;

    /**
     * 是否有效
     */
    @TableField("enabled")
    private Boolean enabled;

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

    public static final String NAME = "name";

    public static final String CITY_ID = "city_id";

    public static final String TELEPHONE = "telephone";

    public static final String SALER_ID = "saler_id";

    public static final String SOURCE = "source";

    public static final String ENABLED = "enabled";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
