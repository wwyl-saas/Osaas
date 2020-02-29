package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Notice;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 消息通知表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-09-01
 */
public interface NoticeMapper extends BaseMapper<Notice> {

    @SqlParser(filter = true)
    @Select("select * from t_notice where merchant_id=#{merchantId} ")
    List<Notice> getNoticeListByMerchant(Long merchantId);
}
