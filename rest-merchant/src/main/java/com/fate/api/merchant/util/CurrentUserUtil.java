package com.fate.api.merchant.util;

import com.fate.common.entity.MerchantUser;
import com.fate.common.enums.UserRoleType;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * @program: parent
 * @description: 当前用户信息
 * @author: chenyixin
 * @create: 2019-07-18 01:07
 **/
public class CurrentUserUtil {
    private static final ThreadLocal<MerchantUser> userHolder = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, UserRoleType>> shopRoleHolder = new ThreadLocal<>();

    public static void addMerchantUser(MerchantUser user){
        userHolder.set(user);
    }

    public static void addShopRoleMap(Map<String, UserRoleType> map){
        shopRoleHolder.set(map);
    }

    /**
     * 获取当前用户基本信息
     * @return
     */
    public static MerchantUser getMerchantUser(){
        MerchantUser user=userHolder.get();
        Assert.notNull(user,"当前线程不存在用户信息");
        return user;
    }

    /**
     * 获取当前用户角色信息
     * @return
     */
    public static UserRoleType getMerchantUserRoleType(Long shopId){
        Map<String, UserRoleType> userRole=shopRoleHolder.get();
        Assert.notNull(userRole,"当前线程不存在用户信息");
        UserRoleType roleType=userRole.get(shopId.toString());
        return roleType;
    }


    /**
     * 获取当前用户角色信息
     * @return
     */
    public static Map<String, UserRoleType> getMerchantUserRoleTypeMap(){
        Map<String, UserRoleType> userRole=shopRoleHolder.get();
        Assert.notNull(userRole,"当前线程不存在用户信息");
        return userRole;
    }

    /**
     *
     */
    public static void remove()
    {
        userHolder.remove();
        shopRoleHolder.remove();
    }
}
