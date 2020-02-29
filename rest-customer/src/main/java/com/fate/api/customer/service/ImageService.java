package com.fate.api.customer.service;

import com.alibaba.fastjson.JSON;
import com.fate.api.customer.dto.JwtToken;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.entity.Customer;
import com.fate.common.entity.CustomerApplication;
import com.fate.common.entity.Merchant;
import com.fate.common.entity.MerchantApplication;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.exception.BaseException;
import com.fate.common.util.CurrentMerchantUtil;
import com.fate.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.net.URLDecoder;
import java.util.Optional;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-25 23:57
 **/
@Service
@Slf4j
public class ImageService {
    private static final String SHARE_KEY=":share:";
    @Autowired
    MerchantService merchantService;
    @Autowired
    JwtService jwtService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CacheService cacheService;

    /**
     * 准备上下文
     * @param applicationId
     */
    public void prepareApplicationContext(Long applicationId){
        MerchantApplication merchantApplication=merchantService.findApplicationById(applicationId);
        log.info("获取当前应用信息为："+ JSON.toJSONString(merchantApplication));
        Assert.notNull(merchantApplication,"商户应用不存在");
        Assert.isTrue(merchantApplication.getEnabled(),"商户应用过期");
        CurrentApplicationUtil.addMerchantApplication(merchantApplication);
        //商户信息
        Merchant merchant=merchantService.getById(merchantApplication.getMerchantId());
        Assert.notNull(merchant,"应用商户不存在");
        Assert.isTrue(merchant.getEnabled(),"商户过期");
        CurrentMerchantUtil.addMerchant(merchant);

    }

    /**
     * 准备上下文
     * @param token
     */
    public void prepareCustomerContext(Long applicationId,String token){
        JwtToken jwtToken=jwtService.verifierToken(token).orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR));
        //用户信息
        Optional<Customer> customer =Optional.ofNullable(customerService.getByCustomerId(jwtToken.getUserId()));
        CurrentCustomerUtil.addCustomer(customer.orElseThrow(()->new BaseException(ResponseInfo.TOKEN_ERROR)));
        //用户应用关联信息
        CustomerApplication customerApplication=customerService.getCustomerApplicationByCustomerIdAndApplicationId(customer.get().getId(), applicationId);
        Assert.notNull(customerApplication,"用户应用信息不能为空");
        CurrentCustomerUtil.addCustomerApplication(customerApplication);
    }


    /**
     * 创建小程序码scene
     * @param goodsId
     * @param applicationId
     * @param token
     * @return
     */
    public String createScene(Long goodsId, Long applicationId, String token) {
        StringBuilder scene=new StringBuilder().append(goodsId).append("-");
        if (StringUtils.isNotBlank(token)){
            try {
                String tokenString= URLDecoder.decode(token,"utf-8");
                prepareCustomerContext(applicationId,tokenString);
                String random= RandomUtil.getRandomStringLongRange(1L,99999999L);
                cacheService.set(CurrentMerchantUtil.getMerchant().getId()+SHARE_KEY+random,CurrentCustomerUtil.getCustomer().getId(),60480L);
                scene.append(random);
            }catch (Exception e){
                log.error("商品小程序码token异常",e);
            }
        }
        return scene.toString();
    }
}
