package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.WxCardType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户微信卡券表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_wx_card")
public class MerchantWxCard extends Model<MerchantWxCard> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 卡券类型
     */
    @TableField("card_type")
    private WxCardType cardType;

    /**
     * 卡券名称
     */
    @TableField("card_name")
    private String cardName;

    /**
     * 卡券Id
     */
    @TableField("card_id")
    private String cardId;

    /**
     * 下发总量
     */
    @TableField("issue_num")
    private Integer issueNum;

    /**
     * 剩余下发量
     */
    @TableField("residue_num")
    private Integer residueNum;

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

    public static final String MERCHANT_ID = "merchant_id";

    public static final String CARD_TYPE = "card_type";

    public static final String CARD_NAME = "card_name";

    public static final String CARD_ID = "card_id";

    public static final String ISSUE_NUM = "issue_num";

    public static final String RESIDUE_NUM = "residue_num";

    public static final String ENABLED = "enabled";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
