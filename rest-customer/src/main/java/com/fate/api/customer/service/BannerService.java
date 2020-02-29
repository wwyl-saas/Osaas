package com.fate.api.customer.service;

import com.fate.common.dao.BannerDao;
import com.fate.common.entity.Banner;
import com.fate.common.enums.BannerPoisition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @program: parent
 * @description: 广告位相关
 * @author: chenyixin
 * @create: 2019-05-23 08:15
 **/
@Service
@Slf4j
public class BannerService {
    private static final String PRIMARY_SIZE = "-cj420";
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    BannerDao bannerDao;

    /**
     * 获取广告位下的所有广告
     * @param positionId
     * @return
     */
    public List<Banner> getBanners(BannerPoisition positionId){
        List<Banner> banners=bannerDao.getByPositionId(positionId);
        if (CollectionUtils.isNotEmpty(banners)){
            banners.stream().forEach(banner -> banner.setPictureUrl(cdnDomain+banner.getPictureUrl()+PRIMARY_SIZE));
        }
        return banners;
    }
}
