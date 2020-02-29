package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.api.merchant.dto.PageDto;
import com.fate.common.dao.GoodsAttributeDao;
import com.fate.common.entity.GoodsAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: rest-merchant
 * @description: 服务问题
 * @author: xudongdong
 * @create: 2019-10-28 23:14
 **/
@Service
@Slf4j
public class GoodsAttributeService {
    @Resource
    GoodsAttributeDao goodsAttributeDao;

    public void create(GoodsAttribute goodsAttribute){
        Assert.isTrue(goodsAttribute.insert(),"插入数据失败");
    }
    public PageDto<GoodsAttribute> getGoodsAttributeList(long pageIndex,long pageSize){
        IPage page=new Page(pageIndex,pageSize);
        IPage<GoodsAttribute> result = this.goodsAttributeDao.page(page);
        return new PageDto<>(result.getCurrent(),result.getSize(),result.getTotal(),result.getPages(),result.getRecords());
    }
    public void delete(List ids){
        Assert.isTrue(this.goodsAttributeDao.removeByIds(ids),"删除数据失败");
    }
    public void update(GoodsAttribute goodsAttribute){
        Assert.isTrue(this.goodsAttributeDao.saveOrUpdate(goodsAttribute),"更新数据失败");
    }
}
