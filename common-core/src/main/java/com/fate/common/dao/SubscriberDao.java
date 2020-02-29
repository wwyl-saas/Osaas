package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Subscriber;

/**
 * <p>
 * 企业公众号订阅者表（需商户用户订阅） 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-16
 */
public interface SubscriberDao extends IService<Subscriber> {

    Subscriber getByOpenid(String mpOpenId);

    Subscriber getByUnionId(String wxUnionid);
}
