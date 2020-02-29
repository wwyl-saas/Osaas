package com.fate.api.customer.handler;

import com.fate.api.customer.query.LoginQuery;
import com.fate.api.customer.query.TryLoginQuery;
import com.fate.common.entity.Customer;

public interface LoginHandler {

    Customer tryLogin(TryLoginQuery query);

    Customer login(LoginQuery query);
}
