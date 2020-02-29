package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.api.merchant.dto.PageDto;
import com.fate.common.dao.GoodsIssueDao;
import com.fate.common.entity.GoodsIssue;
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
public class GoodsIssueService {
    @Resource
    GoodsIssueDao goodsIssueDao;

    public void create(GoodsIssue goodsIssue){
        Assert.isTrue(goodsIssue.insert(),"插入数据失败");
    }
    public PageDto<GoodsIssue> getGoodsIssueList(long pageIndex,long pageSize){
        IPage page=new Page(pageIndex,pageSize);
        IPage<GoodsIssue> result = this.goodsIssueDao.page(page);
        return new PageDto<>(result.getCurrent(),result.getSize(),result.getTotal(),result.getPages(),result.getRecords());
    }
    public void delete(List ids){
        Assert.isTrue(this.goodsIssueDao.removeByIds(ids),"删除数据失败");
    }
    public void update(GoodsIssue goodsIssue){
        Assert.isTrue(this.goodsIssueDao.saveOrUpdate(goodsIssue),"更新数据失败");
    }
}
