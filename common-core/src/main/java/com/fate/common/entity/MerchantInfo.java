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
 * 商户详情表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_info")
public class MerchantInfo extends Model<MerchantInfo> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商户简称
     */
    @TableField("brief_name")
    private String briefName;

    /**
     * 商户简介
     */
    @TableField("description")
    private String description;

    /**
     * 省
     */
    @TableField("province_id")
    private Integer provinceId;

    /**
     * 城市
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 区县
     */
    @TableField("district_id")
    private Integer districtId;

    /**
     * 具体地址
     */
    @TableField("address")
    private String address;

    /**
     * 社会信用编号
     */
    @TableField("licence")
    private String licence;

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

    public static final String BRIEF_NAME = "brief_name";

    public static final String DESC = "description";

    public static final String PROVINCE_ID = "province_id";

    public static final String CITY_ID = "city_id";

    public static final String DISTRICT_ID = "district_id";

    public static final String ADDRESS = "address";

    public static final String LICENCE = "licence";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
