package com.fate.api.merchant.listener.processor;

import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.service.CustomerAccountService;
import com.fate.api.merchant.service.CustomerService;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.dao.CustomerChargeDao;
import com.fate.common.dao.MerchantShopDao;
import com.fate.common.entity.*;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-23 17:37
 **/

@Component
@Slf4j
public class ChargeSuccessProcessor implements MessageProcessor<MyMessage>{
    @Value("${wx.applet.appId}")
    private String appletId;
    @Resource
    CustomerChargeDao customerChargeDao;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    CustomerService customerService;
    @Autowired
    MerchantUserService merchantUserService;
    @Resource
    MerchantShopDao merchantShopDao;
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public boolean handle(MyMessage message) {
        CustomerCharge charge=customerChargeDao.getById(message.getId());
        Assert.notNull(charge,"充值记录不存在");
        List<MerchantUser> merchantUser=merchantUserService.getManagerUser();
        if (!CollectionUtils.isEmpty(merchantUser)){
            PublicMsgEvent wxPublicEvent = new PublicMsgEvent(this);
            wxPublicEvent.setTemplateId(PublicTemplate.CHARGE_TEMPLATE_ID);

            List<String> openIds=merchantUser.stream().map(user -> user.getWxMpOpenid()).collect(Collectors.toList());
            wxPublicEvent.setOpenIds(openIds);
            List<WxMpTemplateData> wxMpTemplateDatas=new ArrayList<>();
            Customer customer=customerService.getById(charge.getCustomerId());
            wxMpTemplateDatas.add(new WxMpTemplateData("first","刚刚有客户"+customer.getName()+"完成在线充值"));
            if (charge.getShopId()!=null){
                MerchantShop shop=merchantShopDao.getById(charge.getShopId());
                wxMpTemplateDatas.add(new WxMpTemplateData("keyword1",shop!=null?shop.getName():"" ));
            }
            CustomerAccount customerAccount=customerAccountService.findByCustomerId(charge.getCustomerId());
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword2", customerAccount!=null?customerAccount.getMemberLevel().getDesc():""));
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword3", charge.getAmount().toPlainString()+"元"));
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword4", customerAccount.getBalance().toPlainString()+"元"));
            wxMpTemplateDatas.add(new WxMpTemplateData("keyword5", DateUtil.getLocalDateTimeString(charge.getCreateTime())));
            wxMpTemplateDatas.add(new WxMpTemplateData("remark", "点击下方进入小程序数据查看"));
            wxPublicEvent.setTemplateData(wxMpTemplateDatas);
            WxMpTemplateMessage.MiniProgram miniProgram= new WxMpTemplateMessage.MiniProgram(appletId,"pages/data/charge?type=4&titleName=充值会员",true);
            wxPublicEvent.setMiniProgram(miniProgram);
            applicationContext.publishEvent(wxPublicEvent);
        }
        return false;
    }
}
