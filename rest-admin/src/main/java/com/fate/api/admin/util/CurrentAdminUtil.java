package com.fate.api.admin.util;

import com.fate.common.entity.Admin;
import org.springframework.util.Assert;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-05 10:41
 **/
public class CurrentAdminUtil {
    private static final ThreadLocal<Admin> userHolder = new ThreadLocal<>();


    public static void addAdmin(Admin admin){
        userHolder.set(admin);
    }


    /**
     * 获取当前用户基本信息
     * @return
     */
    public static Admin getMerchantUser(){
        Admin admin=userHolder.get();
        Assert.notNull(admin,"当前线程不存在用户信息");
        return admin;
    }

    /**
     * 清空信息
     */
    public static void remove()
    {
        userHolder.remove();
    }
}
