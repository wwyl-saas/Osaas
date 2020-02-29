package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Category;

import java.util.List;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CategoryDao extends IService<Category> {

    /**
     * 需要根据序号排序
     * @return
     */
    List<Category> findEnableAllOrdered();


    IPage<Category> getByMerchantIdPage(Integer pageIndex, Integer pageSize);






}
