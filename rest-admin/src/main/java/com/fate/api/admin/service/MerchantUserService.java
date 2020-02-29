package com.fate.api.admin.service;

import com.fate.common.dao.MerchantUserDao;
import com.fate.common.dao.MerchantUserRoleDao;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 17:29
 **/
@Slf4j
@Service
public class MerchantUserService {
    @Resource
    MerchantUserRoleDao merchantUserRoleDao;
    @Resource
    MerchantUserDao merchantUserDao;

    public List<MerchantUserRole> getAllUserRoles(Long merchantId) {
        return merchantUserRoleDao.getAllUserRole(merchantId);
    }

    public List<MerchantUser> getAllUsers(Long merchantId) {
        return merchantUserDao.getAllUsers(merchantId);
    }

    public void updateBatch(List<MerchantUser> updateUsers) {
        merchantUserDao.updateBatchById(updateUsers,100);
    }
}
