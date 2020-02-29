package com.fate.api.customer.service;

import com.fate.api.customer.dto.CustomerDto;
import com.fate.api.customer.exception.NoAuthException;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.handler.LoginHandler;
import com.fate.api.customer.query.LoginQuery;
import com.fate.api.customer.query.TryLoginQuery;
import com.fate.common.entity.Customer;
import com.fate.common.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @program: parent
 * @description: 登录相关
 * @create: 2019-05-22 11:43
 **/
@Service
@Slf4j
public class LoginService {
    @Autowired
    MerchantService merchantService;
    @Autowired
    CustomerService customerService;
    @Autowired
    MemberService memberService;
    @Autowired
    HandlerFactory handlerFactory;


    /**
     * 尝试登录（减少用户授权）
     * @param query
     * @return
     */
    public CustomerDto tryLogin(TryLoginQuery query) {
        LoginHandler handler=handlerFactory.getLoginHandler();
        Customer customer=handler.tryLogin(query);
        if (customer==null){
            throw new NoAuthException();
        }
        return logAndTranslate(customer);
    }


    /**
     * 授权登录
     * @param query
     * @return
     */
    public CustomerDto login(LoginQuery query) {
        LoginHandler handler=handlerFactory.getLoginHandler();
        Customer customer=handler.login(query);
        Assert.notNull(customer,"用户不存在");
        return logAndTranslate(customer);
    }

    /**
     *  记录最后登录时间并返回用户信息
     * @param customer
     * @return
     */
    private CustomerDto logAndTranslate(Customer customer) {
        customer.setLastLoginTime(LocalDateTime.now());
        customer.setUpdateTime(null);
        customerService.updateCustomer(customer);
        return BeanUtil.mapper(customer, CustomerDto.class);
    }

}