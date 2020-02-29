package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Coupon;

import java.util.List;

/**
 * <p>
 * 卡券表（尚未绑定用户） 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-07
 */
public interface CouponDao extends IService<Coupon> {

    IPage<Coupon> getCouponByNamePage(Integer pageIndex, Integer pageSize,String name);

    List<Coupon> getEnableAll();
}
