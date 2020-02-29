package com.fate.api.customer.service;


import com.fate.api.customer.dto.AttributeDto;
import com.fate.api.customer.dto.MemberCouponDto;
import com.fate.api.customer.dto.MemberDetailDto;
import com.fate.api.customer.dto.MemberWelfareDto;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CustomerConsumeDao;
import com.fate.common.dao.MemberDao;
import com.fate.common.dao.MemberThresholdDao;
import com.fate.common.dao.MemberWelfareDao;
import com.fate.common.entity.*;
import com.fate.common.enums.*;
import com.fate.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: parent
 * @description: 会员相关
 * @author: chenyixin
 * @create: 2019-06-07 17:58
 **/
@Service
@Slf4j
public class MemberService {
    private static final String LOCK_ICON="welfare.png";
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    CustomerConsumeDao customerConsumeDao;
    @Resource
    MemberDao memberDao;
    @Resource
    MemberWelfareDao memberWelfareDao;
    @Resource
    MemberThresholdDao memberThresholdDao;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    CouponService couponService;

    /**
     * 获取会员福利信息
     * @return
     */
    public MemberWelfareDto getMemberWelfare() {
        Customer customer= CurrentCustomerUtil.getCustomer();
        CustomerAccount account=customerAccountService.findByCustomerId(customer.getId());
        Assert.notNull(account,"会员账户不能为空");

        MemberWelfareDto result=MemberWelfareDto.builder()
                .consumeNum(account.getConsumeNum())
                .consumeScore(account.getConsumeScore())
                .cumulativeRecharge(account.getCumulativeRecharge() )
                .currentLevel(account.getMemberLevel().getCode())
                .build();

        List<Member> members = memberDao.list();
        Assert.notEmpty(members,"会员信息为空");
        Map<Long,List<MemberWelfare>> memberWelfareMap=memberWelfareDao.list().stream().collect(Collectors.groupingBy(MemberWelfare::getMemberId));
        Assert.notEmpty(memberWelfareMap,"会员福利信息为空");
        Map<Long,List<MemberThreshold>> memberThresholdMap=memberThresholdDao.list().stream().collect(Collectors.groupingBy(MemberThreshold::getMemberId));
        Assert.notEmpty(memberThresholdMap,"会员门槛信息为空");

        List<MemberDetailDto> memberDetailDtos= members.stream().map(member -> {
            MemberDetailDto memberDetailDto=MemberDetailDto.builder()
                    .memberCardUrl(cdnDomain+member.getPictureUrl())
                    .memberLevel(member.getLevel().getCode())
                    .memberName(member.getLevel().getDesc())
                    .cardTag(getCardTag(member.getLevel().getCode(),account.getMemberLevel().getCode()))
                    .couponFlag(false)
                    .levelConditionTag(getConditionTag(member.getLevel().getCode(),account.getMemberLevel().getCode()))
                    .build();

            //获取等级条件
            List<MemberThreshold>  thresholdList=memberThresholdMap.get(member.getId());
            if (CollectionUtils.isNotEmpty(thresholdList)){
                List<AttributeDto> thresholdDtos=thresholdList.stream().map(memberThreshold -> {
                    AttributeDto attributeDto= AttributeDto.builder().name(memberThreshold.getThresholdKey().getDesc()).unit(memberThreshold.getThresholdValue()).build();
                    switch (memberThreshold.getThresholdKey()){
                        case SCORE_VALUE:
                            attributeDto.setValue(account.getConsumeScore().toString());
                            break;
                        case CUMULATIVE_RECHARGE_AMOUNT:
                            attributeDto.setValue(account.getCumulativeRecharge().toString());
                            break;
                        case CUMULATIVE_CONSUME_NUMBER:
                            attributeDto.setValue(account.getConsumeNum().toString());
                            break;
                        case CUMULATIVE_CONSUME_AMOUNT:
                            BigDecimal cumulativeConsume=customerConsumeDao.getConsumeAmountByCustomerId(customer.getId());
                            attributeDto.setValue(cumulativeConsume.toPlainString());
                            break;
                        case CONSUME_AMOUNT:
                            attributeDto.setValue("0");
                            break;
                    }
                    return attributeDto;
                }).collect(Collectors.toList());
                memberDetailDto.setLevelCondition(thresholdDtos);
            }else{
                Assert.isTrue(account.getMemberLevel().equals(MemberLevel.COMMON),"商户高级会员未配置门槛值");
            }


            //已解锁福利列表
            List<MemberWelfare> memberWelfares=memberWelfareMap.get(member.getId());
            if (CollectionUtils.isNotEmpty(memberWelfares)){
                Map<Integer,MemberWelfare> map=memberWelfares.stream().collect(Collectors.toMap(memberWelfare -> memberWelfare.getWelfareKey().getCode(), memberWelfare -> memberWelfare,(a,b)->a));
                List<AttributeDto> unlockedFares=new ArrayList<>();
                List<AttributeDto> lockedFares=new ArrayList<>();
                Arrays.stream(member.getWelfareAllTypes().split(",")).mapToInt(welfareKey->Integer.parseInt(welfareKey)).forEach(type->{
                    MemberWelfare memberWelfare=map.get(type);
                    MemberWelfareType memberWelfareType = MemberWelfareType.getEnum(type).orElseThrow(()->new BaseException(ResponseInfo.ENUM_ERROR));
                    AttributeDto attributeDto = null;
                    if (memberWelfare!=null){//已解锁
                        attributeDto= AttributeDto.builder().name(memberWelfareType.getDesc()).unit(memberWelfare.getUnit()).build();
                        if (memberWelfareType.equals(MemberWelfareType.COUPON)){
                            memberDetailDto.setCouponFlag(true);
                            attributeDto.setValue(cdnDomain+ memberWelfareType.getIcon());
                        }else{
                            attributeDto.setValue(memberWelfare.getWelfareValue());
                        }
                        unlockedFares.add(attributeDto);
                    }else { //未解锁
                        attributeDto= AttributeDto.builder().name(memberWelfareType.getDesc()).value(cdnDomain+LOCK_ICON).unit("图").build();
                        lockedFares.add(attributeDto);
                    }
                });
                memberDetailDto.setUnlockedFares(unlockedFares);
                memberDetailDto.setLockedFares(lockedFares);
            }else{
                Assert.isTrue(account.getMemberLevel().equals(MemberLevel.COMMON),"商户高级会员未配置福利值");
            }


            //会员礼包
            if (memberDetailDto.getCouponFlag()){
                List<MemberWelfare> couponWelfares=memberWelfares.stream().filter(memberWelfare -> memberWelfare.getWelfareKey().equals(MemberWelfareType.COUPON))
                        .filter(memberWelfare -> StringUtils.isNotBlank(memberWelfare.getWelfareValue()))
                        .collect(Collectors.toList());
                List<MemberCouponDto> memberCouponDtos= Lists.emptyList();
                if (CollectionUtils.isNotEmpty(couponWelfares)){
                    memberCouponDtos=couponWelfares.stream().map(welfare -> {
                        String[] array=welfare.getWelfareValue().split(",");
                        Coupon coupon=couponService.getCouponById(Long.parseLong(array[0]));
                        Assert.notNull(coupon,"优惠券不存在");
                        MemberCouponDto couponDto=MemberCouponDto.builder()
                                .couponId(coupon.getId())
                                .couponName(coupon.getName())
                                .number(Integer.parseInt(array[1]))
                                .tag(coupon.getTag()).build();
                        return couponDto;
                    }).collect(Collectors.toList());
                }
                memberDetailDto.setCoupons(memberCouponDtos);
            }

            Optional<MemberThreshold> threshold= thresholdList.stream().filter(memberThreshold -> memberThreshold.getThresholdKey().equals(MemberThresholdType.CHARGE_AMOUNT)).findFirst();
            threshold.ifPresent(hold->{
                memberDetailDto.setChargeButton(getChargeButton(memberDetailDto.getMemberLevel(),memberDetailDto.getMemberName(),result.getCurrentLevel(),hold.getThresholdValue()));
                memberDetailDto.setChargeAmount(hold.getThresholdValue());
            });

            return memberDetailDto;
        }).collect(Collectors.toList());


        result.setDetailDtoList(memberDetailDtos);
        return result;
    }


    private String getCardTag(Integer memberLevel,Integer currentLevel){
        if (memberLevel<currentLevel){
            return "已升级";
        }else if (memberLevel>currentLevel){
            return "待升级";
        }else {
            return "当前等级";
        }
    }

    private String getConditionTag(Integer memberLevel,Integer currentLevel){
        if (memberLevel<=currentLevel){
            return "已达标";
        }else {
            return "未达标";
        }
    }

    private String getChargeButton(Integer memberLevel,String levelName,Integer currentLevel,String needRecharge ){
        if (memberLevel>currentLevel){
            StringBuilder stringBuilder=new StringBuilder("充值").append(needRecharge).append("元可以直接升级为").append(levelName);
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * 根据等级获取会员等级信息
     * @param memberLevel
     * @return
     */
    public Member getByLevel(MemberLevel memberLevel) {
        return memberDao.findByMemberLevel(memberLevel.getCode());
    }

    /**
     * 根据ID查询会员等级
     * @param memberId
     * @return
     */
    public Member getById(Long memberId) {
        return memberDao.getById(memberId);
    }
}
