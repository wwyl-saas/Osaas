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
 * 商户会员福利表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_member")
public class Member extends Model<Member> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 级别
     */
    @TableField("level")
    private MemberLevel level;

    /**
     * 会员卡图片
     */
    @TableField("picture_url")
    private String pictureUrl;

    /**
     * 会员卡图片微信素材
     */
    @TableField("wx_picture_url")
    private String wxPictureUrl;

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
     * 某会员级别对应的所有福利的key
     */
    @TableField("welfare_all_types")
    private String welfareAllTypes;

    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String LEVEL = "level";

    public static final String PICTURE_URL = "picture_url";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
