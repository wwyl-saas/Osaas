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
 * 消息通知表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_notice")
public class Notice extends Model<Notice> {

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
     * 通知类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 通知者
     */
    @TableField("name")
    private String name;

    /**
     * 通知者头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 消息主题
     */
    @TableField("subject")
    private String subject;

    /**
     * 消息内容html格式
     */
    @TableField("content")
    private String content;

    /**
     * 是否已读
     */
    @TableField("is_read")
    private Boolean isRead;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String TYPE = "type";

    public static final String NAME = "name";

    public static final String AVATAR = "avatar";

    public static final String SUBJECT = "subject";

    public static final String CONTENT = "content";

    public static final String ISREAD = "is_read";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
