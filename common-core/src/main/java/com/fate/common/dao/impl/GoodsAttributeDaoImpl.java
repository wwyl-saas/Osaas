package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.GoodsAttributeDao;
import com.fate.common.entity.GoodsAttribute;
import com.fate.common.entity.GoodsIssue;
import com.fate.common.mapper.GoodsAttributeMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 服务规格表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class GoodsAttributeDaoImpl extends ServiceImpl<GoodsAttributeMapper, GoodsAttribute> implements GoodsAttributeDao {

    @Override
    public List<GoodsAttribute> getOrderedByGoodsId(Long goodsId) {
        return baseMapper.selectList(new QueryWrapper<GoodsAttribute>().eq(GoodsAttribute.GOODS_ID,goodsId).last("order by "+GoodsAttribute.SORT_ORDER));
    }

    @Override
    public void removeByGoodsId(Long goodsId) {
        baseMapper.delete(new QueryWrapper<GoodsAttribute>().eq(GoodsAttribute.GOODS_ID,goodsId));
    }

    @Override
    public void removeByGoodsIds(List<Long> goodsIds) {
        baseMapper.delete(new QueryWrapper<GoodsAttribute>().in(GoodsAttribute.GOODS_ID,goodsIds));
    }
}
