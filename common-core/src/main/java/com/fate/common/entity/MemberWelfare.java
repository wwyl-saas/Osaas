package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.MemberWelfareType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员卡固有属性信息表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_member_welfare")
public class MemberWelfare extends Model<MemberWelfare> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 会员福利ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 属性键值
     */
    @TableField("welfare_key")
    private MemberWelfareType welfareKey;

    /**
     * 属性value
     */
    @TableField("welfare_value")
    private String welfareValue;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MEMBER_ID = "member_id";

    public static final String WELFARE_KEY = "welfare_key";

    public static final String WELFARE_VALUE = "welfare_value";

    public static final String UNIT = "unit";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}