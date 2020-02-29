package com.fate.common.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.MerchantPostTitle;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户岗位职称表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantPostTitleDao extends IService<MerchantPostTitle> {

    List<MerchantPostTitle> getAll();

    IPage<MerchantPostTitle> getPostTitlePage(Integer pageIndex, Integer pageSize);


    List<Map<String, Object>> getPostTitlesByIds(List<Long> postTitleIds);
}
