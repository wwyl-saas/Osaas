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
 * 公告表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_announce")
public class Announce extends Model<Announce> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 公告类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 公告名称
     */
    @TableField("name")
    private String name;

    /**
     * 公告内容html格式
     */
    @TableField("content")
    private String content;

    /**
     * 标签
     */
    @TableField("tag")
    private String tag;

    /**
     * 背景图
     */
    @TableField("picture_url")
    private String pictureUrl;

    /**
     * 是否开启
     */
    @TableField("enable")
    private Boolean enable;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String TYPE = "type";

    public static final String NAME = "name";

    public static final String CONTENT = "content";

    public static final String TAG = "tag";

    public static final String PICTURE_URL = "picture_url";

    public static final String ENABLE = "enable";

    public static final String CREATE_TIME = "create_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
