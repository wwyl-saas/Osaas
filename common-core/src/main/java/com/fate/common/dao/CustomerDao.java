package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerConsume;
import com.fate.common.enums.CustomerSource;
import com.fate.common.enums.MemberLevel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * C端用户表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface CustomerDao extends IService<Customer> {
    Customer getByUnionId(String unionId);

    Customer getByOpenId(String openid,Long applicationId,Long merchantId);

    Integer getNewCustomerCount(LocalDate data);

    Customer getByPhone(String customerPhone);

    IPage<Customer> getPageByMobileAndName(Long pageIndex, Long pageSize, String mobile, String customerName, Long merchantId);

    IPage<Customer> getPageByMobileAndNameAndLevelAndSource(Long pageIndex, Long pageSize, String mobile, String customerName, MemberLevel memberLevel, CustomerSource customerSource, Long merchantId);

    Integer getVisitCount(LocalDate date);

    IPage<Customer> getPageBy(List<Long> customerIds, Integer pageIndex, Integer pageSize);

    IPage<Customer> getPageByCreateDate(LocalDate date, Integer pageIndex, Integer pageSize);

    IPage<Customer> getpageByLastloginDate(LocalDate date, Integer pageIndex, Integer pageSize);

    IPage<Customer> getPageByMobile(Long pageIndex, Long pageSize, String mobile);

    List<Map<String, Object>> getCustmerNamesByIds(List<Long> customerIdsList);

    Integer getVisitCount(Long merchantId, LocalDate yesterday);

    Integer getNewCustomerCount(Long merchantId, LocalDate yesterday);


}
