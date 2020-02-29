package com.fate.api.customer;

import com.fate.common.entity.Merchant;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.CurrentMerchantUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: parent
 * @description:
 * @author: chenyixin
 * @create: 2019-09-13 20:33
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@Slf4j
public class TestFeign {

    @Test
    public void testCreateOrder(){
        Merchant merchant= new Merchant().setId(1L);
        CurrentMerchantUtil.addMerchant(merchant);
    }

}
