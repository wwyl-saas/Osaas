package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商户门店表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_merchant_shop")
@Builder
public class MerchantShop extends Model<MerchantShop> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 门店名称
     */
    @TableField("name")
    private String name;

    /**
     * 联系电话
     */
    @TableField("telephone")
    private String telephone;

    /**
     * 门店地址
     */
    @TableField("address")
    private String address;

    /**
     * 门店照片
     */
    @TableField("picture")
    private String picture;

    /**
     * 经度坐标
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 纬度坐标
     */
    @TableField("latitude")
    private Double latitude;

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

    public static final String NAME = "name";

    public static final String TELEPHONE = "telephone";

    public static final String ADDRESS = "address";

    public static final String PICTURE = "picture";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String ENABLED = "enabled";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
