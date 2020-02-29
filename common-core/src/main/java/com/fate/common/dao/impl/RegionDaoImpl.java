package com.fate.common.dao.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate.common.dao.RegionDao;
import com.fate.common.entity.Region;
import com.fate.common.mapper.RegionMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mybatis-plus
 * @since 2019-06-02
 */
@Repository
public class RegionDaoImpl extends ServiceImpl<RegionMapper, Region> implements RegionDao {

}
