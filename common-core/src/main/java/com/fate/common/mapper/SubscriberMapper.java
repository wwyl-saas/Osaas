package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Subscriber;

/**
 * <p>
 * 企业公众号订阅者表（需商户用户订阅） Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-16
 */
@SqlParser(filter = true)
public interface SubscriberMapper extends BaseMapper<Subscriber> {

}
