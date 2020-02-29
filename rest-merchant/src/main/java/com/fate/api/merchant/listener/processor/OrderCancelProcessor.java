package com.fate.api.merchant.listener.processor;

import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.dao.OrderDao;
import com.fate.common.entity.MerchantUser;
import com.fate.common.entity.Order;
import com.fate.common.enums.OrderStatus;
import com.fate.common.model.MyMessage;
import com.fate.common.util.DateUtil;
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
public class OrderCancelProcessor implements MessageProcessor<MyMessage>{
    @Value("${wx.applet.appId}")
    private String appletId;
    @Resource
    OrderDao orderDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public boolean handle(MyMessage message) {
        Order order=orderDao.getById(message.getId());
        Assert.notNull(order,"订单不存在");
        if (order.getStatus().equals(OrderStatus.CANCELED)){
            //通知商户用户
            PublicMsgEvent publicMsg = new PublicMsgEvent(this);
            MerchantUser user = merchantUserService.getMerchantUserById(order.getMerchantUserId());
            Assert.notNull(user, "下单商户用户不存在");
            publicMsg.setOpenIds(Arrays.asList(user.getWxMpOpenid()));
            publicMsg.setTemplateId(PublicTemplate.ORDER_CANCEL_TEMPLATE_ID);
            List<WxMpTemplateData> wxMpTemplateDatas=new ArrayList<>();
            wxMpTemplateDatas.add(new WxMpTemplateData("first","你有一个待支付的订单已被客户取消"));
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword1",order.getOrderNo()));
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword2", DateUtil.getLocalDateTimeString(order.getUpdateTime())));
            wxMpTemplateDatas.add(new WxMpTemplateData("remark","点击下方进入商户端小程序查看详情"));
            publicMsg.setTemplateData(wxMpTemplateDatas);

            WxMpTemplateMessage.MiniProgram miniProgram=new WxMpTemplateMessage.MiniProgram(appletId,"pages/settle/list",true);
            publicMsg.setMiniProgram(miniProgram);
            applicationContext.publishEvent(publicMsg);
        }
        return false;
    }
}
