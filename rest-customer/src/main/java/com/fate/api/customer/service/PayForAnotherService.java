package com.fate.api.customer.service;

import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.dao.CustomerAccountDao;
import com.fate.common.dao.PayForAnotherDao;
import com.fate.common.entity.CustomerAccount;
import com.fate.common.entity.PayForAnother;
import com.fate.common.util.IdUtil;
import com.fate.common.util.QRAndBarCodeUtil;
import com.fate.common.util.QiNiuUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 代付相关
 * @author: songjignhuan
 * @create: 2019-05-27 15:45
 **/
@Service
@Slf4j
public class PayForAnotherService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    PayForAnotherDao payForAnotherDao;
    @Resource
    CustomerAccountDao customerAccountDao;

    /**
     * 生成代人支付码
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public String payForAnotherQRCode(){//todo 产生formid通知用户
        CustomerAccount customerAccount=customerAccountDao.findByCustomerId(CurrentCustomerUtil.getCustomer().getId());
        Assert.notNull(customerAccount,"请先领取会员卡成为会员");

        Long payForAnotherCode= IdUtil.nextId();
        PayForAnother pfa=new PayForAnother();
        pfa.setApplicationId(CurrentApplicationUtil.getMerchantApplication().getId());
        pfa.setPayCustomerId(CurrentCustomerUtil.getCustomer().getId());
        pfa.setPayCode(payForAnotherCode);
        Assert.isTrue(pfa.insert(),"数据插入失败");
        return payForAnotherCode.toString();
    }



}
