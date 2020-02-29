package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.common.entity.Customer;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * C端用户表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerMapper extends BaseMapper<Customer> {
    @SqlParser(filter = true)
    IPage<Customer> getPageByCustomer(Page page, @Param("customer") Customer customer);

    IPage<Customer> getPageByMobile(Page page, @Param("mobile")String mobile);

    List<Map<String, Object>> getCustmerNamesByIds(List<Long> customerIdsList);
}
