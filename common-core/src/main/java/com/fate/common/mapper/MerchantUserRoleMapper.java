package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MerchantUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商户用户角色表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantUserRoleMapper extends BaseMapper<MerchantUserRole> {

    List<Long> getMerchantUserIdsByShopId(Long shopId);

    @SqlParser(filter = true)
    MerchantUserRole getMerchantRoleByUserId(@Param("userId") Long userId, @Param("merchantId")Long merchantId);

    List<Long> queryCandidatesByRole(@Param("roleType")Integer roleType, @Param("shopId")Long shopId);

    List<Long> queryUsersByRole(@Param("roleType")Integer roleType, @Param("shopId")Long shopId);
}
