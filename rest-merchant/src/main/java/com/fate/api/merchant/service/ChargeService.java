package com.fate.api.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate.api.merchant.config.wx.WxMpConfiguration;
import com.fate.api.merchant.dto.ChargeDto;
import com.fate.api.merchant.dto.CustomerAccountDto;
import com.fate.api.merchant.dto.GoodsDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.util.CurrentUserUtil;
import com.fate.common.dao.*;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.util.BeanUtil;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateMessage;
import me.chanjar.weixin.mp.bean.membercard.WxMpMemberCardUpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 充值相关
 * @author: chenyixin
 * @create: 2019-07-28 01:34
 **/
@Service
@Slf4j
public class ChargeService {
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
    @Resource
    MemberThresholdDao memberThresholdDao;
    @Resource
    MemberDao memberDao;
    @Resource
    MemberWelfareDao memberWelfareDao;
    @Autowired
    CouponService couponService;
    @Resource
    MerchantApplicationDao merchantApplicationDao;
    @Resource
    MerchantWxCardDao merchantWxCardDao;

    public CustomerCharge getById(Long chargeID) {
        return customerChargeDao.getById(chargeID);
    }

    /**
     *  线下充值
     * @param shopId
     * @param customerId
     * @param amount
     * @param giftAmount
     * @param userId
     * @return
     */
    public CustomerAccountDto chargeOffline(Long shopId, Long customerId, BigDecimal amount, BigDecimal giftAmount, Long userId) {
        CustomerAccount customerAccount=customerAccountService.findByCustomerId(customerId);
        Assert.notNull(customerAccount,"请用户领取会员卡后充值");
        CustomerCharge charge=completeCharge(customerAccount,shopId,customerId,amount,giftAmount,userId);
        handlerCharge(charge,customerAccount);
        return BeanUtil.mapper(customerAccount,CustomerAccountDto.class);
    }

    /**
     * 完成充值
     * @param customerAccount
     * @param shopId
     * @param customerId
     * @param amount
     * @param giftAmount
     * @param userId
     * @return
     */
    @Transactional
    public CustomerCharge completeCharge(CustomerAccount customerAccount,Long shopId, Long customerId, BigDecimal amount, BigDecimal giftAmount, Long userId){
        MerchantUser merchantUser=CurrentUserUtil.getMerchantUser();
        customerAccount.setBalance(customerAccount.getBalance().add(amount).add(giftAmount));
        customerAccount.setCumulativeRecharge(customerAccount.getCumulativeRecharge().add(amount));
        customerAccount.setChargeNum(customerAccount.getChargeNum()+1);
        customerAccount.setUpdateTime(null);
        Assert.isTrue(customerAccount.updateById(),"更新数据失败");

        CustomerCharge charge=new CustomerCharge();
        charge.setShopId(shopId)
                .setMerchantUserId(userId!=null?userId: merchantUser.getId())
                .setChargeStatus(ChargeStatus.COMPLETE)
                .setCustomerId(customerId)
                .setAmount(amount)
                .setGiftAmount(giftAmount)
                .setChargeType(ChargeType.OFFLINE_CHARGE)
                .setOperatorId(merchantUser.getId());
        Assert.isTrue(charge.insert(),"插入数据失败");
        return charge;
    }

    /**
     * 获取今日充值数量
     * @param shopId
     * @return
     */
    public Integer getTodayNewChargeCount(Long shopId) {
        return customerChargeDao.getNewChargeCountBy(shopId, LocalDate.now());
    }

    /**
     * 实际充值金额
     * @param shopId
     * @return
     */
    public BigDecimal getTodayFactCharge(Long shopId) {
        return customerChargeDao.getFactCharge(shopId,LocalDate.now(), null);
    }

    public BigDecimal getTodayGiftCharge(Long shopId) {
        return customerChargeDao.getTodayGiftCharge(shopId,LocalDate.now(), null);
    }

    public BigDecimal getTodayFactCharge(Long shopId, Long userId) {
        return customerChargeDao.getFactCharge(shopId,LocalDate.now(),userId);
    }

    public BigDecimal getTodayGiftCharge(Long shopId, Long userId) {
        return customerChargeDao.getTodayGiftCharge(shopId,LocalDate.now(),userId);
    }

    public IPage<CustomerCharge> getPageByShopAndDate(Long shopId, Long userId, LocalDate date, Integer pageIndex, Integer pageSize) {
        return customerChargeDao.getPageBy(shopId,userId,date,pageIndex,pageSize);
    }

    public List<Long> getCustomerIdsBy(Long shopId, LocalDate date) {
        return customerChargeDao.getCustomerIdsBy(shopId,date);
    }

    /**
     * 获取充值详情
     * @param chargeId
     * @return
     */
    public ChargeDto getDetail(Long chargeId) {
        CustomerCharge charge=customerChargeDao.getById(chargeId);
        Assert.notNull(charge,"充值记录不存在");
        ChargeDto result=BeanUtil.mapper(charge,ChargeDto.class);
        Customer customer=customerService.getById(charge.getCustomerId());
        Assert.notNull(customer,"用户不存在");
        result.setCustomerName(customer.getName());

        if (charge.getShopId()!=null){//门店后台充值
            MerchantShop merchantshop=merchantShopDao.getById(charge.getShopId());
            Assert.notNull(merchantshop,"商户门店不存在");
            result.setShopName(merchantshop.getName());
            MerchantUser merchantUser=merchantUserService.getById(charge.getMerchantUserId());
            Assert.notNull(merchantUser,"商户用户不存在");
            result.setMerchantUserName(merchantUser.getName());
            if (charge.getMerchantUserId().equals(charge.getOperatorId())){
                result.setOperatorName(merchantUser.getName());
            }else{
                MerchantUser operator=merchantUserService.getById(charge.getOperatorId());
                Assert.notNull(operator,"商户用户不存在");
                result.setOperatorName(operator.getName());
            }
        }
        return result;
    }

    public List<CustomerCharge> getSumByShopId(Long shopId, LocalDate date) {
        return customerChargeDao.getGroupSumByShopId(shopId,date);
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

    /**
     * 通过Id查询充值记录
     * @param chargeId
     * @return
     */
    public CustomerCharge getCustomerChargeById(Long chargeId) {
        return customerChargeDao.getById(chargeId);
    }


    /**
     * 根据商户id查询所有店铺的充值情况
     * @param
     * @return
     */
    public PageDto<ChargeDto> getChargeList(String shopIds, Integer pageIndex, Integer pageSize) {
       List<ChargeDto> result=new ArrayList<>();
        IPage<CustomerCharge> page;
        if(StringUtils.isEmpty(shopIds) || StringUtils.isBlank(shopIds)){
           page=customerChargeDao.getPageByMerchant(pageIndex,pageSize);
       }else{
           List<String> shopIdsStr= Arrays.asList(shopIds.split(","));
           List<Long> list=new ArrayList<Long>();
           for(String str:shopIdsStr){
               list.add(Long.valueOf(str));
           }
           page=customerChargeDao.getPageByShopIds(list);
       }

        if (page != null && CollectionUtils.isNotEmpty(page.getRecords())) {
            List<CustomerCharge> customerCharges = page.getRecords();
            result = customerCharges.stream().map(customerCharge -> {
                ChargeDto chargeDto = BeanUtil.mapper(customerCharge, ChargeDto.class);

                return chargeDto;
            }).collect(Collectors.toList());
            return new PageDto<>(pageIndex, pageSize, page.getTotal(), page.getPages(), result);
        }

        return new PageDto<>(pageIndex, pageSize, 0, 0, result);
    }















}
