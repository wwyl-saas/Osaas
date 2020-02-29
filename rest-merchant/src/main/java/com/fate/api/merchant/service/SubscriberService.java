package com.fate.api.merchant.service;

import com.fate.common.dao.SubscriberDao;
import com.fate.common.entity.Subscriber;
import com.fate.common.enums.Gender;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @program: parent
 * @description: 服务号订阅者
 * @author: chenyixin
 * @create: 2019-09-16 18:13
 **/
@Service
@Slf4j
public class SubscriberService {
    @Resource
    SubscriberDao subscriberDao;

    /**
     * 创建订阅者并保存
     * @param wxMpUser
     * @return
     */
    public Subscriber createSubcriber(WxMpUser wxMpUser) {
        Subscriber subscriber=new Subscriber()
                .setAvatar(wxMpUser.getHeadImgUrl())
//                .setCityId(wxMpUser.getCity())
                .setGender(Gender.getEnum(wxMpUser.getSex()))
                .setNickname(wxMpUser.getNickname())
                .setSource(wxMpUser.getSubscribeScene())
                .setSubscribe(wxMpUser.getSubscribe())
                .setWxOpenid(wxMpUser.getOpenId())
                .setWxUnoinid(wxMpUser.getUnionId());
        Assert.isTrue(subscriber.insert(),"插入数据失败");
        return subscriber;
    }

    /**
     * 根据openID查询订阅者
     * @param mpOpenId
     * @return
     */
    public Subscriber getByOpenid(String mpOpenId) {
        return subscriberDao.getByOpenid(mpOpenId);
    }

    public Subscriber getByUnionId(String wxUnionid) {
        return subscriberDao.getByUnionId(wxUnionid);
    }
}
