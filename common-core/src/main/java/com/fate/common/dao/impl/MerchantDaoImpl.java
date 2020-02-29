package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.MerchantDao;
import com.fate.common.entity.Merchant;
import com.fate.common.mapper.MerchantMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class MerchantDaoImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantDao {

    @Override
    public List<Merchant> getAll(boolean enable) {
        return baseMapper.selectList(new QueryWrapper<Merchant>().eq(Merchant.ENABLED,enable));
    }

    @Override
    public List<Map<String,Object>> getMerchantIdAndName() {
        return baseMapper.getMerchantIdAndName();

    }
}
