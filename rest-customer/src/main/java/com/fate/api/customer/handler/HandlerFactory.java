package com.fate.api.customer.handler;

import com.fate.api.customer.handler.wx.applet.WaChargeHandler;
import com.fate.api.customer.handler.wx.applet.WaEventHandler;
import com.fate.api.customer.handler.wx.applet.WaLoginHandler;
import com.fate.api.customer.handler.wx.applet.WaPayHandler;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.enums.ApplicationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: parent
 * @description: 不同客户端的处理器工厂
 * @author: chenyixin
 * @create: 2019-06-21 10:46
 **/
@Slf4j
@Component
public class HandlerFactory {
    @Autowired
    WaLoginHandler waLoginHandler;
    @Autowired
    WaPayHandler waPayHandler;
    @Autowired
    WaEventHandler waEventHandler;
    @Autowired
    WaChargeHandler waChargeHandler;

    /**
     * 获取登录处理器
     * @return
     */
    public LoginHandler getLoginHandler(){
        ApplicationType type= CurrentApplicationUtil.getMerchantApplication().getType();
        switch (type){
            case WX_APPLET:
                return waLoginHandler;
        }
        return null;
    }

    /**
     * 获取支付处理器
     * @return
     */
    public PayHandler getPayHandler() {
        ApplicationType type=CurrentApplicationUtil.getMerchantApplication().getType();
        switch (type){
            case WX_APPLET:
                return waPayHandler;
        }
        return null;
    }

    /**
     * 消息事件处理器
     * @return
     */
    public EventHandler getEventHandler() {
        ApplicationType type=CurrentApplicationUtil.getMerchantApplication().getType();
        switch (type){
            case WX_APPLET:
                return waEventHandler;
        }
        return null;
    }

    /**
     * 会员充值处理器
     * @return
     */
    public ChargeHandler getChargeHandler() {
        ApplicationType type=CurrentApplicationUtil.getMerchantApplication().getType();
        switch (type){
            case WX_APPLET:
                return waChargeHandler;
        }
        return null;
    }
}
