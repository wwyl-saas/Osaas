package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.GoodsIssueDao;
import com.fate.common.entity.GoodsIssue;
import com.fate.common.mapper.GoodsIssueMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 服务问题表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class GoodsIssueDaoImpl extends ServiceImpl<GoodsIssueMapper, GoodsIssue> implements GoodsIssueDao {

    @Override
    public List<GoodsIssue> getByGoodsId(Long goodsId) {
        return baseMapper.selectList(new QueryWrapper<GoodsIssue>().eq(GoodsIssue.GOODS_ID,goodsId));
    }

    @Override
    public void removeByGoodsId(Long goodsId) {
        baseMapper.delete(new QueryWrapper<GoodsIssue>().eq(GoodsIssue.GOODS_ID,goodsId));
    }

    @Override
    public void removeByGoodIds(List<Long> goodsIds) {
        baseMapper.delete(new QueryWrapper<GoodsIssue>().in(GoodsIssue.GOODS_ID,goodsIds));
    }


}
