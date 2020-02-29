package com.fate.api.customer.service;

import com.fate.api.customer.config.wx.WxMpConfiguration;
import com.fate.api.customer.config.wx.WxPayConfiguration;
import com.fate.api.customer.dto.PayDto;
import com.fate.api.customer.handler.ChargeHandler;
import com.fate.api.customer.handler.EventHandler;
import com.fate.api.customer.handler.HandlerFactory;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.exception.BaseException;
import com.fate.common.model.MqProperties;
import com.fate.common.model.MyMessage;
import com.fate.common.util.CurrentMerchantUtil;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateMessage;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: parent
 * @description: 充值相关
 * @author: chenyixin
 * @create: 2019-06-14 15:48
 **/
@Service
@Slf4j
public class ChargeService {
    @Resource
    MemberThresholdDao memberThresholdDao;
    @Resource
    MemberWelfareDao memberWelfareDao;
    @Resource
    CustomerChargeDao customerChargeDao;
    @Resource
    MemberDao memberDao;
    @Autowired
    CouponService couponService;
    @Resource
    MerchantApplicationDao merchantApplicationDao;
    @Resource
    MerchantWxCardDao merchantWxCardDao;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    HandlerFactory handlerFactory;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    MqSendService mqSendService;


    /***
     * 会员充值
     *
     * @param chargeAmount
     * @return
     */
    public PayDto memberCharge(BigDecimal chargeAmount) {
        MerchantApplication application = CurrentApplicationUtil.getMerchantApplication();
        Customer customer = CurrentCustomerUtil.getCustomer();
        CustomerCharge charge = createCustomerChargeLog(chargeAmount, customer.getId(), application.getId());
        ChargeHandler chargeHandler = handlerFactory.getChargeHandler();
        PayDto result = chargeHandler.preparePay(charge);
        result.setPayId(charge.getId());
        return result;
    }

    /**
     * 通过Id查询充值记录
     * @param chargeId
     * @return
     */
    public CustomerCharge getCustomerChargeById(Long chargeId) {
        return customerChargeDao.getById(chargeId);
    }

    /**
     * 查询充值记录结果
     *
     * @param chargeId
     * @return
     */
    public Boolean queryChargePay(Long chargeId) {
        Boolean result = false;
        CustomerCharge charge = getCustomerChargeById(chargeId);
        Assert.notNull(charge, "微信回调信息所属充值记录不存在");
        if (charge.getChargeStatus().equals(ChargeStatus.WAITING)) { //需要幂等
            ChargeHandler chargeHandler = handlerFactory.getChargeHandler();
            result = chargeHandler.queryChargeResult(charge);
            if (result) {
                completeCharge(charge);
                //通知用户
                EventHandler eventHandler = handlerFactory.getEventHandler();
                eventHandler.pushQueryChargeSuccess(charge);
                //通知商户
                MyMessage myMessage=new MyMessage(chargeId);
                MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.CHARGE_SUCCESS.getTag()).build();
                mqSendService.sendToMerchant(myMessage,mqProperties);
            }
        } else {
            if (charge.getChargeStatus().equals(ChargeStatus.COMPLETE)) {
                log.info("当前订单已经支付,notify={}", result.toString());
                result = true;
            } else {
                log.error("当前支付订单order={}状态不正确，支付结果为result={}", charge, result);
                result = false;
            }
        }
        return result;
    }

    /**
     * 处理微信充值结果通知
     *
     * @param xmlData
     * @param chargeId
     */
    public void consumeWxData(String xmlData, Long chargeId) {
        CustomerCharge charge = getCustomerChargeById(chargeId);
        Assert.notNull(charge, "微信回调信息所属充值记录不存在");
        WxPayService wxPayService = WxPayConfiguration.getPayService(CurrentApplicationUtil.getMerchantApplication().getAppId());
        WxPayOrderNotifyResult notifyResult;
        try {
            notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        } catch (WxPayException e) {
            log.error("微信回调信息校验失败", e);
            throw new BaseException(ResponseInfo.WX_VALIDATE_ERROR);
        }
        if (charge.getChargeStatus().equals(ChargeStatus.WAITING)) { //需要幂等
            Integer payment = charge.getAmount().multiply(BigDecimal.valueOf(100)).intValue();
            if (payment.equals(notifyResult.getTotalFee())) {
                completeCharge(charge);
                //通知用户
                EventHandler eventHandler = handlerFactory.getEventHandler();
                eventHandler.pushNotifyChargeSuccess(charge);
                //通知商户
                MyMessage myMessage=new MyMessage(chargeId);
                MqProperties mqProperties=MqProperties.builder().merchantId(CurrentMerchantUtil.getMerchant().getId()).messageTag(MessageTag.CHARGE_SUCCESS.getTag()).build();
                mqSendService.sendToMerchant(myMessage,mqProperties);
            } else {
                log.error("充值记录和微信支付金额不一致，order={},notify={}", charge.toString(), notifyResult.toString());
            }
        } else {
            log.error("当前订单已经支付,notify={}", notifyResult.toString());
        }
    }

    /**
     * 微信充值--完成充值操作
     *
     * @param charge
     */
    @Transactional
    protected void completeCharge(CustomerCharge charge) {
        //修改充值记录状态
        charge.setChargeStatus(ChargeStatus.COMPLETE);
        charge.setUpdateTime(null);
        Assert.isTrue(charge.updateById(), "更新充值记录失败");

        //修改账户余额
        CustomerAccount customerAccount = customerAccountService.findByCustomerId(charge.getCustomerId());
        customerAccount.setBalance(charge.getAmount().add(charge.getGiftAmount()).add(customerAccount.getBalance()));
        customerAccount.setCumulativeRecharge(customerAccount.getCumulativeRecharge().add(charge.getAmount()));
        customerAccount.setChargeNum(customerAccount.getChargeNum() + 1);
        customerAccount.setUpdateTime(null);
        Assert.isTrue(customerAccount.updateById(), "更新充值记录失败");

        handlerCharge(charge, customerAccount);
    }

    /**
     * 会员卡升级
     *
     * @param customerCharge
     * @param account
     */
    private void handlerCharge(CustomerCharge customerCharge, CustomerAccount account) {
        try {
            if (!account.getMemberLevel().equals(MemberLevel.DIAMOND)) {//非黑钻还能升级
                //判断下一级（有充值跳跃升级）
                MemberThreshold fitMemberThreshold = null;
                List<MemberThreshold> memberThresholds = memberThresholdDao.getByThresholdType(MemberThresholdType.CHARGE_AMOUNT);
                if (CollectionUtils.isNotEmpty(memberThresholds)) {
                    fitMemberThreshold = memberThresholds.stream().filter(memberThreshold -> customerCharge.getAmount().compareTo(BigDecimal.valueOf(Double.parseDouble(memberThreshold.getThresholdValue()))) >= 0)
                            .reduce(((memberThreshold1, memberThreshold2) -> {
                                if (Double.parseDouble(memberThreshold1.getThresholdValue()) >= Double.parseDouble(memberThreshold2.getThresholdValue())) {
                                    return memberThreshold1;
                                } else {
                                    return memberThreshold2;
                                }
                            })).get();
                }
                //确定下一等级
                boolean flag = false;
                Member nextMember;
                if (fitMemberThreshold != null) {
                    flag = true;
                    nextMember = memberDao.getById(fitMemberThreshold.getMemberId());
                    Assert.notNull(nextMember, "会员等级不存在" + fitMemberThreshold.getMemberId());
                } else {
                    MemberLevel memberLevel = MemberLevel.getEnum(account.getMemberLevel().getCode() + 1).get();
                    nextMember = memberDao.findByMemberLevel(memberLevel.getCode());
                    Assert.notNull(nextMember, "会员等级未设置" + memberLevel.getDesc());
                }

                if (!flag) {
                    List<MemberThreshold> allMemberThresholds = memberThresholdDao.getByMemberId(nextMember.getId());
                    Assert.notEmpty(allMemberThresholds, "会员升级规则未设置");
                    for (MemberThreshold memberThreshold : allMemberThresholds) {
                        switch (memberThreshold.getThresholdKey()) {
                            case CUMULATIVE_RECHARGE_AMOUNT:
                                BigDecimal chargeAmount = BigDecimal.valueOf(Double.parseDouble(memberThreshold.getThresholdValue()));
                                if (account.getCumulativeRecharge().compareTo(chargeAmount) >= 0) {
                                    flag = true;
                                }
                                break;
                            case CHARGE_AMOUNT:
                                BigDecimal chargeOneAmount = BigDecimal.valueOf(Double.parseDouble(memberThreshold.getThresholdValue()));
                                if (customerCharge.getAmount().compareTo(chargeOneAmount) >= 0) {
                                    flag = true;
                                }
                                break;
                            default:
                                break;
                        }
                        if (flag) {
                            break;
                        }
                    }

                }

                if (flag) {//升级并发放礼包
                    log.info("充值成功判断会员升级{}", customerCharge);
                    account.setMemberId(nextMember.getId());
                    account.setMemberLevel(nextMember.getLevel());
                    account.setUpdateTime(null);
                    Assert.isTrue(account.updateById(), "插入数据失败");

                    //判断礼包
                    List<MemberWelfare> memberWelfares = memberWelfareDao.getByMemberIdAndType(nextMember.getId(), MemberWelfareType.COUPON);
                    if (CollectionUtils.isNotEmpty(memberWelfares)) {
                        dispatchCoupons(memberWelfares, customerCharge.getCustomerId(), customerCharge.getMerchantAppId());
                    }

                    //修改微信会员卡信息
                    String appid = getPublicAppid();
                    if (StringUtils.isNotBlank(appid)) {
                        WxMpService wxMpService = WxMpConfiguration.getMpService(getPublicAppid());
                        Assert.notNull(wxMpService, "微信公众号配置不正确");
                        MerchantWxCard merchantWxCard = merchantWxCardDao.getByCardType(WxCardType.MEMBER_CARD);
                        Assert.notNull(merchantWxCard, "无有效的会员卡套");

                        WxMpMemberCardUpdateMessage updateMessage = new WxMpMemberCardUpdateMessage();
                        updateMessage.setCardId(merchantWxCard.getCardId());
                        updateMessage.setCode(account.getWxCardCode());
                        //更新卡面
                        if (StringUtils.isNotBlank(nextMember.getWxPictureUrl())) {
                            updateMessage.setBackgroundPicUrl(nextMember.getWxPictureUrl());
                        }
                        updateMessage.setCustomFieldValue1(nextMember.getLevel().getDesc());

                        WxMpMemberCardUpdateResult updateResult = null;
                        try {
                            updateResult = wxMpService.getMemberCardService().updateUserMemberCard(updateMessage);
                            Assert.isTrue(updateResult.getErrorCode().equals("0"), "更新会员卡返回失败" + updateResult.toString());
                        } catch (WxErrorException e) {
                            log.error("调用微信更新会员卡接口报错", e);
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error("会员升级报错", e);
        }
    }

    /**
     * 下发优惠券
     *
     * @param memberWelfares
     */
    private void dispatchCoupons(List<MemberWelfare> memberWelfares, Long customerId, Long applicationId) {
        memberWelfares.stream().forEach(memberWelfare -> {
            String[] array = memberWelfare.getWelfareValue().split(",");
            if (array.length > 1) {
                Coupon coupon = couponService.getCouponById(Long.parseLong(array[0]));
                Integer number = Integer.parseInt(array[1]);
                if (coupon != null) {
                    for (int i = 0; i < number; i++) {
                        couponService.dispatchCoupon(coupon, customerId, applicationId);
                    }
                }
            }
        });
    }



    /**
     * 生产充值订单记录
     * @param amount
     * @param customerId
     * @param applicationId
     * @return
     */
    public CustomerCharge createCustomerChargeLog(BigDecimal amount, Long customerId, Long applicationId) {
        Assert.notNull(amount,"参数不能为空");
        Assert.notNull(customerId,"参数不能为空");
        Assert.notNull(applicationId,"参数不能为空");

        CustomerCharge charge=new CustomerCharge();
        charge.setAmount(amount)
                .setChargeStatus(ChargeStatus.WAITING)
                .setChargeType(ChargeType.ONLINE_WX_CHARGE)
                .setCustomerId(customerId)
                .setMerchantAppId(applicationId);
        boolean flag=charge.insert();
        Assert.isTrue(flag,"生产充值订单记录失败");
        return charge;
    }


    /**
     * 获取公众号ID
     *
     * @return
     */
    private String getPublicAppid() {
        MerchantApplication merchantApplication = merchantApplicationDao.getMpApplication(CurrentMerchantUtil.getMerchant().getId());
        if (merchantApplication != null) {
            return merchantApplication.getAppId();
        }
        return null;
    }

}
