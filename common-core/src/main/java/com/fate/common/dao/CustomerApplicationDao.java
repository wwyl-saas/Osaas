package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.CustomerApplication;

/**
 * <p>
 * C端用户应用来源表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-25
 */
public interface CustomerApplicationDao extends IService<CustomerApplication> {

    CustomerApplication getByApplicationIdAndOpenId(String openid, Long id);

    CustomerApplication getByCustomerIdAndApplicationId(Long customerId, Long applicationId);
}
