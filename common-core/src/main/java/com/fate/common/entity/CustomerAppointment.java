package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.AppointmentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * C端用户预约表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer_appointment")
public class CustomerAppointment extends Model<CustomerAppointment> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 来源应用ID
     */
    @TableField("merchant_app_id")
    private Long merchantAppId;

    /**
     * 商铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 门店名称
     */
    @TableField("shop_name")
    private String shopName;

    /**
     * C端用户ID
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 到达日期
     */
    @TableField("arrive_date")
    private LocalDate arriveDate;

    /**
     * 到达时间
     */
    @TableField("arrive_time")
    private LocalTime arriveTime;

    /**
     * 预约人名称
     */
    @TableField("appoint_name")
    private String appointName;

    /**
     * 预约人手机号
     */
    @TableField("appoint_mobile")
    private String appointMobile;

    /**
     * 预约人头像
     */
    @TableField("appoint_avatar")
    private String appointAvatar;

    /**
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * 服务项目ID
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态
     */
    @TableField("status")
    private AppointmentStatus status;

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

    public static final String SHOP_ID = "shop_id";

    public static final String SHOP_NAME = "shop_name";

    public static final String CUSTOMER_ID = "customer_id";

    public static final String ARRIVE_DATE = "arrive_date";

    public static final String ARRIVE_TIME = "arrive_time";

    public static final String APPOINT_NAME = "appoint_name";

    public static final String APPOINT_MOBILE = "appoint_mobile";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String GOODS_ID = "goods_id";

    public static final String GOODS_NAME = "goods_name";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
