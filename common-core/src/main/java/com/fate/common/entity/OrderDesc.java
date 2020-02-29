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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单详情表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_order_desc_")
public class OrderDesc extends Model<OrderDesc> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

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
     * 简图URL
     */
    @TableField("brief_pic_url")
    private String briefPicUrl;

    /**
     * 商品名称
     */
    @TableField("name")
    private String name;

    /**
     * 商品简介
     */
    @TableField("goods_brief")
    private String goodsBrief;

    /**
     * 专柜价格
     */
    @TableField("counter_price")
    private BigDecimal counterPrice;

    /**
     * 市场价格
     */
    @TableField("market_price")
    private BigDecimal marketPrice;

    /**
     * 产品数量
     */
    @TableField("goods_num")
    private Integer goodsNum;

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

    public static final String ORDER_ID = "order_id";

    public static final String GOODS_ID = "goods_id";

    public static final String BRIEF_PIC_URL = "brief_pic_url";

    public static final String NAME = "name";

    public static final String GOODS_BRIEF = "goods_brief";

    public static final String COUNTER_PRICE = "counter_price";

    public static final String MARKET_PRICE = "market_price";

    public static final String GOODS_NUM = "goods_num";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
