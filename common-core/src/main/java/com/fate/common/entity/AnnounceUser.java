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
 * 公告查看表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_announce_user")
public class AnnounceUser extends Model<AnnounceUser> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * 公告ID
     */
    @TableField("announce_id")
    private Long announceId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String ANNOUNCE_ID = "announce_id";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
