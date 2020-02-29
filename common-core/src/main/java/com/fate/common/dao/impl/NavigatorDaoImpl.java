package com.fate.common.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.NavigatorDao;
import com.fate.common.entity.Navigator;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.enums.UserRoleType;
import com.fate.common.mapper.NavigatorMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * <p>
 * 商户首页导航表 服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
@Repository
public class NavigatorDaoImpl extends ServiceImpl<NavigatorMapper, Navigator> implements NavigatorDao {
    @Override
    public List<Navigator> getNavigatorsByRole(UserRoleType roleType, Long merchantId) {
        Assert.notNull(roleType, ResponseInfo.PARAM_NULL.getMsg());
        Assert.notNull(merchantId, ResponseInfo.PARAM_NULL.getMsg());
        return baseMapper.getNavigatorsByRole(roleType.getCode(),merchantId);
    }
}
