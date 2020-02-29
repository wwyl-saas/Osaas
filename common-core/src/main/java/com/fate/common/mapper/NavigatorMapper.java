package com.fate.common.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.Navigator;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商户首页导航表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-07-19
 */
public interface NavigatorMapper extends BaseMapper<Navigator> {
    @SqlParser(filter = true)
    @Select("SELECT t.id,t.icon,t.color,t.`type`,t.url FROM t_navigator t inner JOIN t_merchant_role_navigator t1 ON t.id = t1.navigator_id\n" +
            " WHERE t1.merchant_id = #{merchantId} AND t1.role_type = #{roleId}")
    List<Navigator> getNavigatorsByRole(@Param("roleId") Integer roleId,@Param("merchantId") Long merchantId);
}
