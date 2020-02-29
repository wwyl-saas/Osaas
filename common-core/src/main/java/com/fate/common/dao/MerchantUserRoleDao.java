package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import com.fate.common.enums.UserRoleType;

import java.util.List;

/**
 * <p>
 * 商户用户角色表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantUserRoleDao extends IService<MerchantUserRole> {

    List<MerchantUserRole> getMerchantUserByShopId(Long shopId);

    List<Long> getMerchantUserIdsByShopId(Long shopId);

    void removeByUserId(Long userId,Long shopId);

    List<MerchantUserRole> getByUserId(Long userId);

    /**
     * 获取门店级别的权限
     * @param shopId
     * @param userId
     * @return
     */
    MerchantUserRole getShopRoleByShopAndUserId(Long shopId, Long userId);

    /**
     * 获取商户级别权限
     * @param userId
     * @return
     */
    MerchantUserRole getMerchantRoleByUserId(Long userId);
    /**
     * 获取商户级别权限
     * @param userId
     * @return
     */
    MerchantUserRole getMerchantRoleByUserId(Long userId, Long merchantId);





    /**
     * 根据商户ID和角色获取员工信息
     * @param pageIndex
     * @param pageSize
     * @param roleType
     * @return
     */
    IPage<MerchantUserRole> getByMerchantByRoleTypePage(Integer pageIndex, Integer pageSize, Integer roleType);

    /**
     *查找某角色的候选人
     * @param roleType
     * @param shopId
     * @return
     */
    List<Long> queryCandidatesByRole(Integer roleType, Long shopId);


    /**
     * 查找某角色的员工
     * @param roleType
     * @param shopId
     * @return
     */
    List<Long> queryUsersByRole(Integer roleType, Long shopId);

    /**
     * 根据商铺id删除人员角色
     * @param id
     */
    void removeByShopId(Long id);

    List<MerchantUserRole> getByRoleType(List<UserRoleType> roles);

    List<MerchantUserRole> getAllUserRole(Long merchantId);
}
