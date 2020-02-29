package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.MemberThresholdType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员门槛值属性信息表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_member_threshold")
public class MemberThreshold extends Model<MemberThreshold> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 门槛键值
     */
    @TableField("threshold_type")
    private MemberThresholdType thresholdKey;

    /**
     * 门槛值val
     */
    @TableField("threshold_value")
    private String thresholdValue;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MEMBER_ID = "member_id";

    public static final String THRESHOLD_KEY = "threshold_type";

    public static final String THRESHOLD_VALUE = "threshold_value";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
