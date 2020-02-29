package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.PushMessage;
import com.fate.common.enums.BusinessType;

/**
 * <p>
 * 延迟推送消息表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-18
 */
public interface PushMessageDao extends IService<PushMessage> {

    PushMessage getByBusinessIdAndType(Long id, BusinessType type);
}
