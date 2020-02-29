package com.fate.api.customer.util;

import com.fate.common.entity.MerchantApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @program: parent
 * @description: 当前应用信息
 * @author: chenyixin
 * @create: 2019-07-18 01:10
 **/
@Slf4j
public class CurrentApplicationUtil {
    private static final ThreadLocal<MerchantApplication> applicationHolder = new ThreadLocal<>();

    public static void addMerchantApplication(MerchantApplication merchantApplication){
        applicationHolder.set(merchantApplication);
    }

    public static MerchantApplication getMerchantApplication(){
        MerchantApplication merchantApplication=applicationHolder.get();
        Assert.notNull(merchantApplication,"当前线程不存在商户信息，请确保和servlet在同一线程中获取");
        return merchantApplication;
    }


    public static void remove(){
        applicationHolder.remove();
    }
}
