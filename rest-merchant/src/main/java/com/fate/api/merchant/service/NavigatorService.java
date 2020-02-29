package com.fate.api.merchant.service;

import com.fate.api.merchant.dto.NavigatorDto;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.dao.NavigatorDao;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import com.fate.common.entity.Navigator;
import com.fate.common.enums.NavigatorType;
import com.fate.common.enums.UserRoleType;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 首页导航相关
 * @author: chenyixin
 * @create: 2019-07-19 09:42
 **/
@Service
@Slf4j
public class NavigatorService {
    @Resource
    NavigatorDao navigatorDao;
    @Resource
    MerchantUserRoleDao merchantUserRoleDao;
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderService orderService;
    @Autowired
    NoticeService noticeService;

    /**
     * 获取导航列表
     * @return
     */
    public List<NavigatorDto> getList(Long shopId) {
        List<NavigatorDto> restult= Lists.emptyList();
        UserRoleType roleType=CurrentUserUtil.getMerchantUserRoleType(shopId);
        List<Navigator> navigators=navigatorDao.getNavigatorsByRole(roleType, CurrentMerchantUtil.getMerchant().getId());
        if (CollectionUtils.isNotEmpty(navigators)){
            restult=navigators.stream().map(navigator -> {
                NavigatorDto navigatorDto= BeanUtil.mapper(navigator,NavigatorDto.class);
                navigatorDto.setName(navigator.getType());
                navigatorDto.setBadge(0);
                if (navigator.getType().equals(NavigatorType.APPOINTMENT)){//未确认的预约
                    navigatorDto.setBadge(appointmentService.getWaitingAppointmentCount(shopId));
                }else if (navigator.getType().equals(NavigatorType.ACHIEVEMENT)){
                    navigatorDto.setBadge(1);
                }else if (navigator.getType().equals(NavigatorType.NOTICE)){
                    navigatorDto.setBadge(noticeService.getNoticeNumber());
                }
                return navigatorDto;
            }).collect(Collectors.toList());
        }
        return restult;
    }



}
