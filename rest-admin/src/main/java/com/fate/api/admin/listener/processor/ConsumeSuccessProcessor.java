package com.fate.api.admin.listener.processor;

import com.fate.api.admin.config.wx.WxMpConfiguration;
import com.fate.api.admin.service.*;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.MemberLevel;
import com.fate.common.enums.MemberWelfareType;
import com.fate.common.enums.WxCardType;
import com.fate.common.model.MyMessage;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateMessage;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 账户变动
 * @author: chenyixin
 * @create: 2019-09-19 13:43
 **/
@Component
@Slf4j
public class ConsumeSuccessProcessor implements MessageProcessor<MyMessage>{
    @Autowired
    CustomerConsumeService customerConsumeService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    MemberService memberService;
    @Autowired
    CouponService couponService;
    @Resource
    MerchantApplicationDao merchantApplicationDao;
    @Resource
    MerchantWxCardDao merchantWxCardDao;


    @Override
    public boolean handle(MyMessage message) {
        Long merchantId=CurrentMerchantUtil.getMerchant().getId();
        log.info("收到消费成功变动消息");
        CustomerConsume customerConsume=customerConsumeService.getById(message.getId(),merchantId);
        Assert.notNull(customerConsume,"充值记录不存在");
        CustomerAccount account=customerAccountService.findByCustomerId(customerConsume.getCustomerId(),merchantId);
        Assert.notNull(account,"用户未领取会员卡");

        //会员卡升级判断
        boolean flag=false;
        Member nextMember = null;
        if (!account.getMemberLevel().equals(MemberLevel.DIAMOND)){ //非黑钻还能升级
            MemberLevel memberLevel=MemberLevel.getEnum(account.getMemberLevel().getCode()+1).get();

            nextMember=memberService.getByLevel(memberLevel.getCode(),merchantId);
            Assert.notNull(nextMember,"会员等级未设置"+memberLevel.getDesc());
            List<MemberThreshold> memberThresholds=memberService.getThresholdByMemberId(nextMember.getId(),merchantId);
            Assert.notEmpty(memberThresholds,"会员升级规则未设置");
            for (MemberThreshold memberThreshold:memberThresholds){
                switch (memberThreshold.getThresholdKey()){
                    case SCORE_VALUE:
                        Integer score=Integer.parseInt(memberThreshold.getThresholdValue());
                        if (account.getConsumeScore()>= score){
                            flag=true;
                        }
                        break;
                    case CUMULATIVE_CONSUME_AMOUNT:
                        BigDecimal consumeAmount=BigDecimal.valueOf(Double.parseDouble(memberThreshold.getThresholdValue()));
                        if (account.getCumulativeConsume().compareTo(consumeAmount)>=0){
                            flag=true;
                        }
                        break;
                    case CUMULATIVE_CONSUME_NUMBER:
                        Integer consumeTime=Integer.parseInt(memberThreshold.getThresholdValue());
                        if (account.getConsumeNum()>= consumeTime){
                            flag=true;
                        }
                        break;
                    case CONSUME_AMOUNT:
                        BigDecimal consumeOneAmount=BigDecimal.valueOf(Double.parseDouble(memberThreshold.getThresholdValue()));
                        if (customerConsume.getAmount().compareTo(consumeOneAmount)>=0){
                            flag=true;
                        }
                        break;
                    default:
                        //do nothing
                }
                if (flag){
                    break;
                }
            }
            if (flag){ //升级并发放礼包
                log.info("消费成功判断会员升级{}",customerConsume);
                //改account
                account.setMemberId(nextMember.getId());
                account.setMemberLevel(nextMember.getLevel());
                account.setUpdateTime(null);
                Assert.isTrue(account.updateById(),"插入数据失败");
                //判断礼包
                List<MemberWelfare> memberWelfares=memberService.getByMemberIdAndType(nextMember.getId(), MemberWelfareType.COUPON,merchantId);
                if (CollectionUtils.isNotEmpty(memberWelfares)){
                    dispatchCoupons(memberWelfares,customerConsume.getCustomerId(),customerConsume.getMerchantAppId());
                }
            }
        }


        //修改微信会员卡信息
        String appid=getPublicAppid();
        if (StringUtils.isNotBlank(appid)){
            WxMpService wxMpService= WxMpConfiguration.getMpService(getPublicAppid());
            Assert.notNull(wxMpService,"微信公众号配置不正确");
            MerchantWxCard merchantWxCard=merchantWxCardDao.getByCardType(WxCardType.MEMBER_CARD,merchantId);
            Assert.notNull(merchantWxCard,"无有效的会员卡套");

            WxMpMemberCardUpdateMessage updateMessage=new WxMpMemberCardUpdateMessage();
            updateMessage.setCardId(merchantWxCard.getCardId());
            updateMessage.setCode(account.getWxCardCode());
            if (flag && nextMember!=null){//升级换卡面
                if (StringUtils.isNotBlank(nextMember.getWxPictureUrl())){
                    updateMessage.setBackgroundPicUrl(nextMember.getWxPictureUrl());
                }
                updateMessage.setCustomFieldValue1(nextMember.getLevel().getDesc());
            }
            //更新积分
            Integer score=customerConsume.getAmount().intValue();
            updateMessage.setAddBounus(score);
            updateMessage.setBonus(account.getConsumeScore());
            updateMessage.setRecordBonus(String.format("消费%s元赠送%d积分",customerConsume.getAmount().toPlainString(),score));

            WxMpMemberCardUpdateResult updateResult = null;
            try {
                updateResult = wxMpService.getMemberCardService().updateUserMemberCard(updateMessage);
                Assert.isTrue(updateResult.getErrorCode().equals("0"),"更新会员卡返回失败"+updateResult.toString());
            } catch (WxErrorException e) {
                log.error("调用微信更新会员卡接口报错",e);
            }
        }

        return true;
    }

    /**
     * 下发优惠券
     * @param memberWelfares
     */
    private void dispatchCoupons(List<MemberWelfare> memberWelfares,Long customerId,Long applicationId){
        memberWelfares.stream().forEach(memberWelfare -> {
            String[] array=memberWelfare.getWelfareValue().split(",");
            if (array.length>1){
                Coupon coupon=couponService.getById(Long.parseLong(array[0]));
                Integer number=Integer.parseInt(array[1]);
                if (coupon!=null){
                    for (int i=0;i<number;i++){
                        couponService.dispatchCoupon(coupon,customerId,applicationId);
                    }
                }
            }
        });
    }


    /**
     * 获取公众号ID
     * @return
     */
    private String getPublicAppid(){
        MerchantApplication merchantApplication= merchantApplicationDao.getMpApplication(CurrentMerchantUtil.getMerchant().getId());
        if (merchantApplication!=null){
            return merchantApplication.getAppId();
        }
        return null;
    }
}
