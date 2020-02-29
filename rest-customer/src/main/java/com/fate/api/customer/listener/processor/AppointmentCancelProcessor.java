package com.fate.api.customer.listener.processor;

import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.service.MerchantService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.model.MyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @program: parent
 * @description: 预约取消
 * @author: chenyixin
 * @create: 2019-09-23 14:36
 **/
@Slf4j
@Component
public class AppointmentCancelProcessor implements MessageProcessor<MyMessage>{
    @Resource
    CustomerAppointmentDao customerAppointmentDao;
    @Autowired
    MerchantService merchantService;
    @Autowired
    HandlerFactory handlerFactory;

    @Override
    public boolean handle(MyMessage message) {
        CustomerAppointment appointment=customerAppointmentDao.getById(message.getId());
        Assert.notNull(appointment,"预约不存在");
        MerchantApplication merchantApplication=merchantService.findApplicationById(appointment.getMerchantAppId());
        Assert.notNull(merchantApplication,"应用不存在");
        CurrentApplicationUtil.addMerchantApplication(merchantApplication);
        EventHandler eventHandler=handlerFactory.getEventHandler();
        eventHandler.pushCustomerAppointmentCancelMsg(appointment);
        log.info("客户端推送预约取消消息成功");
        CurrentApplicationUtil.remove();
        return false;
    }
}
