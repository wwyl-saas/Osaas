package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 延迟推送消息表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_push_message")
public class PushMessage extends Model<PushMessage> {

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
     * 用户OpenId
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 小程序表单或者payID
     */
    @TableField("form_id")
    private String formId;

    /**
     * 业务ID
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private BusinessType businessType;

    /**
     * 推送次数
     */
    @TableField("push_number")
    private Integer pushNumber;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_APP_ID = "merchant_app_id";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String FORM_ID = "form_id";

    public static final String BUSINESS_ID = "business_id";

    public static final String BUSINESS_TYPE = "business_type";

    public static final String PUSH_NUMBER = "push_number";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
