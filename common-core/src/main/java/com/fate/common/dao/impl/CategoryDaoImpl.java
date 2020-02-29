package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CategoryDao;
import com.fate.common.entity.Category;
import com.fate.common.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class CategoryDaoImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryDao {

    @Override
    public List<Category> findEnableAllOrdered() {
        return baseMapper.selectList(new QueryWrapper<Category>().eq(Category.ENABLE,true).last("order by sort_order"));
    }
   @Override
    public IPage<Category> getByMerchantIdPage(Integer pageIndex, Integer pageSize) {
        return baseMapper.selectPage(new Page<>(pageIndex,pageSize), Wrappers.<Category>lambdaQuery().last("order by sort_order"));
    }


}
