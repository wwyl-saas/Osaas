package com.fate.common.util;

import com.fate.common.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
/**
 * @program: parent
 * @description: 当前商户信息
 * @author: chenyixin
 * @create: 2019-07-18 01:04
 **/
@Slf4j
public class CurrentMerchantUtil {
    private static final ThreadLocal<Merchant> merchantHolder = new ThreadLocal<>();

    public static void addMerchant(Merchant merchant){
        merchantHolder.set(merchant);
    }

    public static Merchant getMerchant(){
        Merchant merchant=merchantHolder.get();
        Assert.notNull(merchant,"当前线程不存在商户信息，请确保和servlet在同一线程中获取");
        return merchant;
    }


    public static void remove(){
        merchantHolder.remove();
    }
}
