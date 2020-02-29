package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.FeedBackType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员反馈表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_feedback")
public class Feedback extends Model<Feedback> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 应用ID
     */
    @TableField("merchant_app_id")
    private Long merchantAppId;

    /**
     * C端用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * C端用户名称
     */
    @TableField("customer_name")
    private String customerName;


    /**
     * C端用户头像
     */
    @TableField("customer_Avatar")
    private String customerAvatar;

    /**
     * 反馈类型
     */
    @TableField("type")
    private FeedBackType type;

    /**
     * 是否匿名
     */
    @TableField("anonymous")
    private Boolean anonymous;

    /**
     * 反馈信息
     */
    @TableField("remark")
    private String remark;

    /**
     * 反馈图片ID,限制3个
     */
    @TableField("picture_urls")
    private String pictureUrls;

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

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String TYPE = "type";

    public static final String ANONYMOUS = "anonymous";

    public static final String REMARK = "remark";

    public static final String PICTURE_URLS = "picture_urls";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
