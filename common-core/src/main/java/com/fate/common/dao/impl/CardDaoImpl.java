package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.CardDao;
import com.fate.common.entity.Card;
import com.fate.common.mapper.CardMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务卡项表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
@Repository
public class CardDaoImpl extends ServiceImpl<CardMapper, Card> implements CardDao {

}
