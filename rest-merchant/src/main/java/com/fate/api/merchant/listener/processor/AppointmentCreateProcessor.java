package com.fate.api.merchant.listener.processor;

import com.fate.api.merchant.cons.PublicTemplate;
import com.fate.api.merchant.event.PublicMsgEvent;
import com.fate.api.merchant.service.MerchantUserService;
import com.fate.common.dao.CustomerAppointmentDao;
import com.fate.common.entity.CustomerAppointment;
import com.fate.common.entity.MerchantUser;
import com.fate.common.model.MyMessage;
import com.fate.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-23 17:35
 **/

@Component
@Slf4j
public class AppointmentCreateProcessor implements MessageProcessor<MyMessage>{
    @Value("${wx.applet.appId}")
    private String appletId;
    @Resource
    CustomerAppointmentDao customerAppointmentDao;
    @Autowired
    MerchantUserService merchantUserService;
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public boolean handle(MyMessage message) {
        CustomerAppointment appointment=customerAppointmentDao.getById(message.getId());
        Assert.notNull(appointment,"预约不存在");
        PublicMsgEvent wxPublicEvent = new PublicMsgEvent(this);
        wxPublicEvent.setTemplateId(PublicTemplate.APPOINTMENT_CREATE_TEMPLATE_ID);
        //预约技师
        MerchantUser merchantUser =null;
        if (appointment.getMerchantUserId() != null) {
            merchantUser = merchantUserService.getMerchantUserById(appointment.getMerchantUserId());
        }
        if (merchantUser != null) {
            List<String> openids = Lists.list(merchantUser.getWxMpOpenid());
            wxPublicEvent.setOpenIds(openids);
        } else {
            List<MerchantUser> merchantUsers = merchantUserService.getNoticeEventMerchantUser(appointment.getShopId());
            if (CollectionUtils.isNotEmpty(merchantUsers)) {
                List<String> openIds = merchantUsers.stream().map(user -> user.getWxMpOpenid()).collect(Collectors.toList());
                wxPublicEvent.setOpenIds(openIds);
            }
        }

        List<WxMpTemplateData> templateData = new ArrayList<>();
        templateData.add(new WxMpTemplateData("first", String.format("您收到一个%s的预约申请，请及时确认",appointment.getShopName())));
        templateData.add(new WxMpTemplateData("keyword1", appointment.getAppointName()));
        templateData.add(new WxMpTemplateData("keyword2", appointment.getAppointMobile()));
        templateData.add(new WxMpTemplateData("keyword3", DateUtil.getLocalDateString(appointment.getArriveDate())+" "+ DateUtil.getLocalTimeString(appointment.getArriveTime())));
        templateData.add(new WxMpTemplateData("keyword4", StringUtils.isNotBlank(appointment.getGoodsName())?appointment.getGoodsName():"到店确认"));
        templateData.add(new WxMpTemplateData("remark", "点击下方进入商户端小程序进行确认操作"));
        wxPublicEvent.setTemplateData(templateData);

        WxMpTemplateMessage.MiniProgram miniProgram=new WxMpTemplateMessage.MiniProgram(appletId,"pages/appointment/list",true);
        wxPublicEvent.setMiniProgram(miniProgram);
        applicationContext.publishEvent(wxPublicEvent);
        return false;
    }
}
