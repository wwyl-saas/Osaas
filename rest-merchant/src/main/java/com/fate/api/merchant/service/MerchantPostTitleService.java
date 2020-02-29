package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.dto.MerchantPostTitleDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.MerchantPostTitleQuery;
import com.fate.common.dao.MerchantPostTitleDao;
import com.fate.common.entity.MerchantPostTitle;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import javax.annotation.Resource;

import java.util.List;

import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 职称相关
 * @author:
 * @create: 2019-06-02 11:00
 **/
@Service
@Slf4j
public class MerchantPostTitleService {
    @Resource
    MerchantPostTitleDao merchantPostTitleDao;


    /**
     * 新建职称
     * @param merchantPostTitleQuery
     */
    public void savePostTitle(MerchantPostTitleQuery merchantPostTitleQuery) {
        MerchantPostTitle merchantPostTitle=new MerchantPostTitle();
        merchantPostTitle.setName(merchantPostTitleQuery.getName());
        merchantPostTitle.setDescription(merchantPostTitleQuery.getDesc());
        Assert.isTrue(merchantPostTitle.insert(),"插入数据失败");

    }

    /**
     * 修改职称
     * @param merchantPostTitleQuery
     */
    public void updatePostTitle(MerchantPostTitleQuery merchantPostTitleQuery) {
        Assert.notNull(merchantPostTitleQuery.getId(),"id不能为空！");
        MerchantPostTitle merchantPostTitle=merchantPostTitleDao.getById(merchantPostTitleQuery.getId());
        Assert.notNull(merchantPostTitle,"职称不存在！");
        merchantPostTitle.setName(merchantPostTitleQuery.getName());
        merchantPostTitle.setDescription(merchantPostTitleQuery.getDesc());
        Assert.isTrue(merchantPostTitle.updateById(),"更新数据失败");


    }

    /**
     * 删除某职称
     * @param id
     */
    public void deletePostTitle(Long id) {
        Assert.notNull(id,"id不能为空！");
        MerchantPostTitle merchantPostTitle=merchantPostTitleDao.getById(id);
        Assert.notNull(merchantPostTitle,"职称不存在！");
        Assert.isTrue(merchantPostTitle.deleteById(),"删除失败！"); ;
    }

    /**
     * 查询商户下的所有职称
     * @param pageIndex
     * @param pageSize
     * @return
     */

    public PageDto<MerchantPostTitleDto> queryPostTitle(Integer pageIndex, Integer pageSize) {
        IPage<MerchantPostTitle> page=merchantPostTitleDao.getPostTitlePage(pageIndex,pageSize);
        List<MerchantPostTitleDto> result= Lists.emptyList();
        if (page!=null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<MerchantPostTitle> merchantPostTitles = page.getRecords();
            result = merchantPostTitles.stream().map(merchantPostTitle -> {
                MerchantPostTitleDto merchantPostTitleDto = BeanUtil.mapper(merchantPostTitle, MerchantPostTitleDto.class);
                return merchantPostTitleDto;
            }).collect(Collectors.toList());
        }
        return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);

    }



}
