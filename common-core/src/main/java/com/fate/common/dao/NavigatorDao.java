package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Navigator;
import com.fate.common.enums.UserRoleType;

import java.util.List;

/**
 * <p>
 * 商户首页导航表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
public interface NavigatorDao extends IService<Navigator> {
    List<Navigator> getNavigatorsByRole(UserRoleType roleType, Long merchantId);
}
