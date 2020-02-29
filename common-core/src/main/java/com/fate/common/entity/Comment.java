package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务评论表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_comment")
public class Comment extends Model<Comment> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商铺ID
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * 产品ID
     */
    @TableField("goods_id")
    private Long goodsId;

    /**
     * 商户用户ID
     */
    @TableField("merchant_user_id")
    private Long merchantUserId;

    /**
     * 商户用户名称
     */
    @TableField("merchant_user_name")
    private String merchantUserName;

    /**
     * 商户用户名称
     */
    @TableField("merchant_user_title")
    private String merchantUserTitle;

    /**
     * 用户名称
     */
    @TableField("customer_name")
    private String customerName;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 评分
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 评论
     */
    @TableField("remark")
    private String remark;

    /**
     * 图片ID,限制3
     */
    @TableField("picture_urls")
    private String pictureUrls;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 是否匿名
     */
    @TableField("is_anonymous")
    private Boolean isAnonymous;


    public static final String ID = "id";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String SHOP_ID = "shop_id";

    public static final String ORDER_ID = "order_id";

    public static final String GOODS_ID = "goods_id";

    public static final String MERCHANT_USER_ID = "merchant_user_id";

    public static final String MERCHANT_USER_NAME = "merchant_user_name";

    public static final String CUSTOMER_NAME = "customer_name";

    public static final String AVATAR = "avatar";

    public static final String GRADE = "grade";

    public static final String REMARK = "remark";

    public static final String PICTURE_URLS = "picture_urls";

    public static final String CREATE_TIME = "create_time";

    public static final String ANONYMOUS = "is_anonymous";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
