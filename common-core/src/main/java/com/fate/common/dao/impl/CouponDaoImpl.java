package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CouponDao;
import com.fate.common.entity.Coupon;
import com.fate.common.mapper.CouponMapper;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 卡券表（尚未绑定用户） 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-07
 */
@Repository
public class CouponDaoImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponDao {

    @Override
    public IPage<Coupon> getCouponByNamePage(Integer pageIndex, Integer pageSize,String name) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<Coupon>();
        if(name!=null){
            queryWrapper.like(Coupon.NAME,name);
        }
        return  baseMapper.selectPage(new Page<>(pageIndex,pageSize),queryWrapper);

    }

    @Override
    public List<Coupon> getEnableAll() {
        return baseMapper.selectList(new QueryWrapper<Coupon>().eq(Coupon.ENABLE,true));
    }
}
