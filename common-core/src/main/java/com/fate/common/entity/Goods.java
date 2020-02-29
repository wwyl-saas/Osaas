package com.fate.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务信息表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_goods")
public class Goods extends Model<Goods> implements Serializable {

private static final long serialVersionUID=1L;
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 简图url
     */
    @TableField("brief_pic_url")
    private String briefPicUrl;

    /**
     * 主图列表
     */
    @TableField("primary_pic_urls")
    private String primaryPicUrls;

    /**
     * 详情图列表
     */
    @TableField("list_pic_urls")
    private String listPicUrls;

    /**
     * 商品简介
     */
    @TableField("goods_brief")
    private String goodsBrief;

    /**
     * 商品描述
     */
    @TableField("goods_desc")
    private String goodsDesc;

    /**
     * 是否上架
     */
    @TableField("on_sale")
    private Boolean onSale;

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
     * 是否热卖
     */
    @TableField("hot")
    private Boolean isHot;

    /**
     * 是否新品
     */
    @TableField("new")
    private Boolean isNew;

    /**
     * 是否推荐
     */
    @TableField("recommend")
    private Boolean isRecommend;

    /**
     * 销售量
     */
    @TableField("sell_volume")
    private Integer sellVolume;

    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 关键字
     */
    @TableField("keywords")
    private String keywords;

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

    public static final String CATEGORY_ID = "category_id";

    public static final String NAME = "name";

    public static final String BRIEF_PIC_URL = "brief_pic_url";

    public static final String PRIMARY_PIC_URLS = "primary_pic_urls";

    public static final String LIST_PIC_URLS = "list_pic_urls";

    public static final String GOODS_BRIEF = "goods_brief";

    public static final String GOODS_DESC = "goods_desc";

    public static final String ON_SALE = "on_sale";

    public static final String COUNTER_PRICE = "counter_price";

    public static final String MARKET_PRICE = "market_price";

    public static final String IS_HOT = "hot";

    public static final String IS_NEW = "new";

    public static final String IS_RECOMMEND = "recommend";

    public static final String SELL_VOLUME = "sell_volume";

    public static final String SORT_ORDER = "sort_order";

    public static final String KEYWORDS = "keywords";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
