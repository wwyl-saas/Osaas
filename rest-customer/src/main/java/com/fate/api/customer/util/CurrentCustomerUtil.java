package com.fate.api.customer.util;

import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerApplication;
import org.springframework.util.Assert;


/**
 * @program: parent
 * @description: 当前用户信息
 * @author: chenyixin
 * @create: 2019-07-18 01:06
 **/
public class CurrentCustomerUtil {
    private static final ThreadLocal<Customer> customerHolder = new ThreadLocal<>();
    private static final ThreadLocal<CustomerApplication> customerApplicationHolder = new ThreadLocal<>();

    public static void addCustomer(Customer customer){
        customerHolder.set(customer);
    }
    public static void addCustomerApplication(CustomerApplication customerApplication){
        customerApplicationHolder.set(customerApplication);
    }
    /**
     * 获取当前用户基本信息
     * @return
     */
    public static Customer getCustomer(){
        Customer customer=customerHolder.get();
        Assert.notNull(customer,"当前线程不存在用户信息，1.请确保和servlet在同一线程中获取 2.请确保当前接口@Auth注解要求用户处于登录态");
        return customer;
    }

    /**
     * 获取当前用户在当前应用下的特殊信息
     * @return
     */
    public static CustomerApplication getCustomerApplication(){
        CustomerApplication customerApplication=customerApplicationHolder.get();
        Assert.notNull(customerApplication,"当前线程不存在用户信息，1.请确保和servlet在同一线程中获取 2.请确保当前接口@Auth注解要求用户处于登录态");
        return customerApplication;
    }



    public static void remove(){
        customerHolder.remove();
        customerApplicationHolder.remove();
    }
}
