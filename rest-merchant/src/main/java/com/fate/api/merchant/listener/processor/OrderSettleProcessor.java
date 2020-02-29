package com.fate.api.merchant.listener.processor;

import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.service.CustomerService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.Customer;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.MyMessage;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-23 17:36
 **/

@Component
@Slf4j
public class OrderSettleProcessor implements MessageProcessor<MyMessage>{
    @Value("${wx.applet.appId}")
    private String appletId;
    @Resource
    OrderDao orderDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ApplicationContext applicationContext;

    @Override
    public boolean handle(MyMessage message) {
        Order order=orderDao.getById(message.getId());
        Assert.notNull(order,"订单不存在");
        MerchantUser user = merchantUserService.getMerchantUserById(order.getMerchantUserId());
        Assert.notNull(user, "下单商户用户不存在");
        //通知商户用户
        PublicMsgEvent publicMsg = new PublicMsgEvent(this);
        publicMsg.setOpenIds(Arrays.asList(user.getWxMpOpenid()));
        if (order.getStatus().equals(OrderStatus.PAYED)){
            publicMsg.setTemplateId(PublicTemplate.PAY_SUCCESS_TEMPLATE_ID);
            List<WxMpTemplateData> templateDatas=new ArrayList<>();
            Customer customer=customerService.getById(order.getCustomerId());
            templateDatas.add(new WxMpTemplateData("first","您有一笔订单收款成功"));
            templateDatas.add(new WxMpTemplateData("keyword1",customer.getName()));
            templateDatas.add(new WxMpTemplateData("keyword2",order.getOrderNo()));
            templateDatas.add(new WxMpTemplateData("keyword3",order.getPayAmount().toPlainString()+"元"));
            templateDatas.add(new WxMpTemplateData("keyword4",order.getGoodsNames()));
            templateDatas.add(new WxMpTemplateData("remark","点击下方进入商户端小程序查看详情"));
            publicMsg.setTemplateData(templateDatas);

            WxMpTemplateMessage.MiniProgram miniProgram=new WxMpTemplateMessage.MiniProgram(appletId,"pages/settle/list",true);
            publicMsg.setMiniProgram(miniProgram);
            applicationContext.publishEvent(publicMsg);
        }else if (order.getStatus().equals(OrderStatus.PAYED_ERROR)){
            publicMsg.setTemplateId(PublicTemplate.PAY_ERROR_TEMPLATE_ID);
            List<WxMpTemplateData> templateDatas=new ArrayList<>();
            templateDatas.add(new WxMpTemplateData("first","您有一笔订单收款失败"));
            templateDatas.add(new WxMpTemplateData("keyword1",order.getPayAmount().toPlainString()+"元"));
            templateDatas.add(new WxMpTemplateData("keyword2",order.getGoodsNames()));
            templateDatas.add(new WxMpTemplateData("keyword3","未知错误"));
            templateDatas.add(new WxMpTemplateData("remark","点击下方进入商户端小程序查看详情"));
            publicMsg.setTemplateData(templateDatas);

            WxMpTemplateMessage.MiniProgram miniProgram=new WxMpTemplateMessage.MiniProgram(appletId,"pages/settle/list",true);
            publicMsg.setMiniProgram(miniProgram);
            applicationContext.publishEvent(publicMsg);
        }
        return false;
    }
}
