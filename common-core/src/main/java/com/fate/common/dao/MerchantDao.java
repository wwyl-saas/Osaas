package com.fate.common.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fate.common.entity.Merchant;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户表 服务类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
public interface MerchantDao extends IService<Merchant> {

    List<Merchant> getAll(boolean enable);

    List<Map<String,Object> > getMerchantIdAndName();

}
