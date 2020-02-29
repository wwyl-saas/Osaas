package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.entity.Goods;
import com.fate.common.entity.MerchantPostTitle;
import com.fate.common.entity.MerchantUser;
import com.fate.common.mapper.MerchantPostTitleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户岗位职称表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantPostTitleDaoImpl extends ServiceImpl<MerchantPostTitleMapper, MerchantPostTitle> implements MerchantPostTitleDao {

    @Override
    public List<MerchantPostTitle> getAll() {
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public IPage<MerchantPostTitle> getPostTitlePage(Integer pageIndex, Integer pageSize) {
        QueryWrapper<MerchantPostTitle> queryWrapper = new QueryWrapper<MerchantPostTitle>();
        queryWrapper.last("order by "+MerchantPostTitle.CREATE_TIME);
        return  baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);

    }

    @Override
    public List<Map<String, Object>> getPostTitlesByIds(List<Long> postTitleIds) {
        return baseMapper.getPostTitlesByIds(postTitleIds);
    }
}
