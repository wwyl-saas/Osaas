package com.fate.common.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户用户表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantUserDao extends IService<MerchantUser> {
    List<MerchantUser> getMerchantUserByIds(List<Long> ids);

    MerchantUser getByAppletOpenId(String openId);

    MerchantUser getByMobile(String phone);

    void updatePsw(Long userId, String psw);

    void updateUser(MerchantUser user);

    /**
     * 在无currentInfo的情况下使用
     * @param userId
     * @return
     */
    MerchantUser getByUserId(Long userId);

    IPage<MerchantUser> getPageByShopId(Integer pageIndex, Integer pageSize,  Long shopId,Long merchantId);

    List<MerchantUser> getByShopId(Long shopId,Long merchantId);

    MerchantUser getByMerchantMobile(String mobile);

    List<MerchantUser> getByShopIdAndName(Long shopId, String userName, Long id);

    IPage<MerchantUser> getPageByMobileAndName(Integer pageIndex, Integer pageSize, String mobile, String merchantUserName,Long merchantId);

    /**
     *根据员工角色查询员工信息
     * @param pageSize
     * @return
     */
    IPage<MerchantUser> queryMerchantUsersByRoleType(Integer pageIndex,Integer pageSize, Integer roleType);


    /**
     * 根据员工id集合查询员工相关信息
     * @param userIds
     * @return
     */
    List<MerchantUser> queryUsersByIds(List<Long> userIds);

    /**
     * 根据服务对应的技师集合
     * @param goodsId
     * @return
     */
    List<Map<String,Object>> getIdAndNameByGoodsId(Long goodsId);

    /**
     * 根据union查用户
     * @param wxUnoinid
     * @return
     */
    MerchantUser getByUnionId(String wxUnoinid);

    /**
     * 根据id列表查询
     * @param userIds
     * @return
     */
    List<MerchantUser> getByUserIds(List<Long> userIds);

    List<MerchantUser> getAllUsers(Long merchantId);
}
