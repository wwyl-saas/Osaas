package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.MerchantUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户用户表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantUserMapper extends BaseMapper<MerchantUser> {
    @SqlParser(filter = true)
    MerchantUser getByMobile(String mobile);
    @SqlParser(filter = true)
    MerchantUser getByAppletOpenId(String openId);
    @SqlParser(filter = true)
    void updateUser(MerchantUser user);
    @SqlParser(filter = true)
    MerchantUser getByUserId(Long userId);
    @SqlParser(filter = true)
    IPage<MerchantUser> getPageByShopId(Page<MerchantUser> page, @Param("userRole")MerchantUserRole userRole);
    @SqlParser(filter = true)
    List<MerchantUser> getByShopId(@Param("shopId") Long shopId, @Param("merchantId") Long merchantId);
    @SqlParser(filter = true)
    List<MerchantUser> getByShopIdAndName(@Param("shopId")Long shopId,@Param("userName") String userName, @Param("merchantId")Long merchantId);

    @SqlParser(filter = true)
    IPage<MerchantUser> getPageByMobileAndName(Page<MerchantUser> Page, @Param("mobile")String mobile, @Param("merchantUserName")String merchantUserName, @Param("merchantId")Long merchantId);

    IPage<MerchantUser> getPageByByRoleType(Page<MerchantUser> Page, @Param("roleType")Integer roleType);

    List<Map<String,Object>> getIdAndNameByGoodsId(Long goodsId);
    @SqlParser(filter = true)
    MerchantUser getByUnionId(String unionId);
}
