package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.PushMessageDao;
import com.fate.common.entity.PushMessage;
import com.fate.common.enums.BusinessType;
import com.fate.common.mapper.PushMessageMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 延迟推送消息表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-18
 */
@Repository
public class PushMessageDaoImpl extends ServiceImpl<PushMessageMapper, PushMessage> implements PushMessageDao {

    @Override
    public PushMessage getByBusinessIdAndType(Long id, BusinessType type) {
        return baseMapper.selectOne(new QueryWrapper<PushMessage>().eq(PushMessage.BUSINESS_ID,id).eq(PushMessage.BUSINESS_TYPE,type));
    }
}
