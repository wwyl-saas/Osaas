package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fate.common.enums.NavigatorType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 商户首页导航表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_navigator")
public class Navigator extends Model<Navigator> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Integer id;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 颜色
     */
    @TableField("color")
    private String color;

    /**
     * 类型
     */
    @TableField("type")
    private NavigatorType type;

    /**
     * 链接
     */
    @TableField("url")
    private String url;


    public static final String ID = "id";

    public static final String ICON = "icon";

    public static final String COLOR = "color";

    public static final String TYPE = "type";

    public static final String URL = "url";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
