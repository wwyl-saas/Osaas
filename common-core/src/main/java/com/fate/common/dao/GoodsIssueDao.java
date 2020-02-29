package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.GoodsAttribute;
import com.fate.common.entity.GoodsIssue;

import java.util.List;

/**
 * <p>
 * 服务问题表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface GoodsIssueDao extends IService<GoodsIssue> {

    List<GoodsIssue> getByGoodsId(Long goodsId);

    void removeByGoodsId(Long goodsId);

    void removeByGoodIds(List<Long> goodsIds);
}
