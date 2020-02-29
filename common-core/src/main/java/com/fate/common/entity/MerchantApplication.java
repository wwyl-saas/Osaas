package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.ApplicationType;
import com.fate.common.enums.MsgFormatType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户app表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_application")
public class MerchantApplication extends Model<MerchantApplication> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 产品名称
     */
    @TableField("name")
    private String name;

    /**
     * 产品类型
     */
    @TableField("type")
    private ApplicationType type;

    /**
     * 入口URL
     */
    @TableField("url")
    private String url;

    /**
     * 三方APPID
     */
    @TableField("app_id")
    private String appId;

    /**
     * 三方APP密钥
     */
    @TableField("app_secret")
    private String appSecret;

    /**
     * 消息服务token
     */
    @TableField("msg_token")
    private String msgToken;

    /**
     * 消息服务AESKey
     */
    @TableField("msg_aes_key")
    private String msgAesKey;

    /**
     * Msg格式类型
     */
    @TableField("msg_format_type")
    private MsgFormatType msgFormatType;

    /**
     * 微信支付商户ID
     */
    @TableField("wx_mch_id")
    private String wxMchId;

    /**
     * 微信支付密钥
     */
    @TableField("wx_partner_key")
    private String wxPartnerKey;

    /**
     * 是否有效
     */
    @TableField("enabled")
    private Boolean enabled;

    /**
     * 启用时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

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

    public static final String NAME = "name";

    public static final String TYPE = "type";

    public static final String URL = "url";

    public static final String APP_ID = "app_id";

    public static final String APP_SECRET = "app_secret";

    public static final String MSG_TOKEN = "msg_token";

    public static final String MSG_AES_KEY = "msg_aes_key";

    public static final String MSG_FORMAT_TYPE = "msg_format_type";

    public static final String WX_MCH_ID = "wx_mch_id";

    public static final String WX_PARTNER_KEY = "wx_partner_key";

    public static final String ENABLED = "enabled";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
