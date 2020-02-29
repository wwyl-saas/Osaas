package com.fate.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate.common.entity.MerchantPostTitle;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户岗位职称表 Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantPostTitleMapper extends BaseMapper<MerchantPostTitle> {

    List<Map<String, Object>> getPostTitlesByIds(List<Long> postTitleIds);
}
