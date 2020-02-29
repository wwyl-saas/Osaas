package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.BannerPoisition;
import com.fate.common.enums.MediaType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 首页banner表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_banner")
@Builder
public class Banner extends Model<Banner> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 位置ID
     */
    @TableField("banner_position_id")
    private BannerPoisition bannerPositionId;

    /**
     * 媒体类型
     */
    @TableField("media_type")
    private MediaType mediaType;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 链接
     */
    @TableField("link")
    private String link;

    /**
     * 图片
     */
    @TableField("picture_url")
    private String pictureUrl;

    /**
     * 是否启用
     */
    @TableField("enable")
    private Boolean enable;

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

    public static final String BANNER_POSITION_ID = "banner_position_id";

    public static final String MEDIA_TYPE = "media_type";

    public static final String NAME = "name";

    public static final String LINK = "link";

    public static final String PICTURE_URL = "picture_url";

    public static final String ENABLE = "enable";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
